package com.hilingual.core.localstorage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class DiaryTempManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val imageStorage: InternalImageStorage
) : DiaryTempManager {

    private fun keyHasDiaryTemp(date: LocalDate) = booleanPreferencesKey("diary_${date}_has_temp")
    private fun keyDiaryText(date: LocalDate) = stringPreferencesKey("diary_${date}_text")
    private fun keyDiaryImageUri(date: LocalDate) = stringPreferencesKey("diary_${date}_image_uri")

    override suspend fun hasDiaryTemp(selectedDate: LocalDate): Boolean {
        return dataStore.data.first()[keyHasDiaryTemp(selectedDate)] ?: false
    }

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ) {
        val hasDiaryContent = text.isNotBlank() || imageUri != null

        if (!hasDiaryContent) {
            clear(selectedDate)
            return
        }

        dataStore.edit { preferences ->
            preferences[keyHasDiaryTemp(selectedDate)] = true
            preferences[keyDiaryText(selectedDate)] = text
            handleImageUpdate(preferences, selectedDate, imageUri)
        }
    }

    override suspend fun getDiaryText(selectedDate: LocalDate): String? {
        return dataStore.data.first()[keyDiaryText(selectedDate)]
    }

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): String? {
        return dataStore.data.first()[keyDiaryImageUri(selectedDate)]
    }

    override suspend fun clear(selectedDate: LocalDate) {
        dataStore.edit { preferences ->
            preferences[keyHasDiaryTemp(selectedDate)] = false
            preferences.remove(keyDiaryText(selectedDate))
            preferences.remove(keyDiaryImageUri(selectedDate))
        }

        dataStore.data.first()[keyDiaryImageUri(selectedDate)]?.let {
            imageStorage.deleteImageFromInternal(it)
        }
    }

    private suspend fun handleImageUpdate(
        preferences: MutablePreferences,
        date: LocalDate,
        newUri: Uri?
    ) {
        val oldImageUriString = preferences[keyDiaryImageUri(date)]

        if (newUri == null) {
            imageStorage.deleteImageFromInternal(oldImageUriString)
            preferences.remove(keyDiaryImageUri(date))
            return
        }

        if (newUri.scheme != "file") {
            val savedUri = imageStorage.saveImageToInternal(newUri)
            preferences[keyDiaryImageUri(date)] =
                savedUri.toString()
            if (oldImageUriString != null && oldImageUriString != savedUri.toString()) {
                imageStorage.deleteImageFromInternal(oldImageUriString)
            }
        }
    }
}

class InternalImageStorage @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun saveImageToInternal(imageUri: Uri): Uri? = withContext(Dispatchers.IO) {
        val directory = File(context.filesDir, "diary_images").apply { mkdirs() }
        val destFile = File(directory, "img_${UUID.randomUUID()}.jpg")

        try {
            context.contentResolver.openInputStream(imageUri).use { input ->
                FileOutputStream(destFile).use { output ->
                    input?.copyTo(output)
                }
            } ?: throw IOException()

            Uri.fromFile(destFile)
        } catch (e: Exception) {
            e.printStackTrace()
            if (destFile.exists()) destFile.delete()
            null
        }
    }

    suspend fun deleteImageFromInternal(imageUriString: String?) = withContext(Dispatchers.IO) {
        if (imageUriString == null) return@withContext

        val path = imageUriString.toUri().path ?: return@withContext

        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }
}
