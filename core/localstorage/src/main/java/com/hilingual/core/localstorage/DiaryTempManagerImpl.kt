package com.hilingual.core.localstorage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class DiaryTempManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val appContext: Context
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

        dataStore.edit { preferences ->
            preferences[keyHasDiaryTemp(selectedDate)] = hasDiaryContent

            if (hasDiaryContent) {
                preferences[keyDiaryText(selectedDate)] = text

                val oldImageUri = preferences[keyDiaryImageUri(selectedDate)]
                if (oldImageUri != null && imageUri != null) {
                    if (imageUri.scheme != "file") {
                        deleteImageFromInternal(oldImageUri)
                    }
                }

                if (imageUri != null) {
                    val newImageUri = saveImageToInternal(imageUri)
                    preferences[keyDiaryImageUri(selectedDate)] = newImageUri.toString()
                } else {
                    preferences.remove(keyDiaryImageUri(selectedDate))
                }
            } else {
                preferences.remove(keyDiaryText(selectedDate))
                preferences.remove(keyDiaryImageUri(selectedDate))
            }
        }
    }

    override suspend fun getDiaryText(selectedDate: LocalDate): String? {
        return dataStore.data.first()[keyDiaryText(selectedDate)]
    }

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): String? {
        return dataStore.data.first()[keyDiaryImageUri(selectedDate)]
    }

    override suspend fun clear(selectedDate: LocalDate) {
        val savedImageUri = dataStore.data.first()[keyDiaryImageUri(selectedDate)]
        if (savedImageUri != null) {
            deleteImageFromInternal(savedImageUri)
        }

        dataStore.edit { preferences ->
            preferences[keyHasDiaryTemp(selectedDate)] = false
            preferences.remove(keyDiaryText(selectedDate))
            preferences.remove(keyDiaryImageUri(selectedDate))
        }
    }

    private fun saveImageToInternal(imageUri: Uri): Uri {
        val directory = File(appContext.filesDir, "diary_images").apply { mkdirs() }
        val destFile = File(directory, "img_${System.currentTimeMillis()}.jpg")

        appContext.contentResolver.openInputStream(imageUri).use { input ->
            FileOutputStream(destFile).use { output ->
                input?.copyTo(output)
            }
        }

        return Uri.fromFile(destFile)
    }

    private fun deleteImageFromInternal(imageUriString: String?) {
        if (imageUriString == null) return

        val fileUri = imageUriString.toUri()
        val path = fileUri.path ?: return

        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }
}
