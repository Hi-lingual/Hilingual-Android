package com.hilingual.core.localstorage

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hilingual.core.localstorage.util.InternalImageStorage
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val imageStorage: InternalImageStorage
) : DiaryTempManager {

    private fun keyIsDiaryTempExist(date: LocalDate) = booleanPreferencesKey("diary_${date}_has_temp")
    private fun keyDiaryText(date: LocalDate) = stringPreferencesKey("diary_${date}_text")
    private fun keyDiaryImageUri(date: LocalDate) = stringPreferencesKey("diary_${date}_image_uri")

    override suspend fun isDiaryTempExist(selectedDate: LocalDate): Boolean {
        return dataStore.data.first()[keyIsDiaryTempExist(selectedDate)] ?: false
    }

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ) {
        val isDiaryContentExist = text.isNotBlank() || imageUri != null

        if (!isDiaryContentExist) {
            clearDiaryTemp(selectedDate)
            return
        }

        dataStore.edit { preferences ->
            preferences[keyIsDiaryTempExist(selectedDate)] = true
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

    override suspend fun clearDiaryTemp(selectedDate: LocalDate) {
        dataStore.data.first()[keyDiaryImageUri(selectedDate)]?.let {
            imageStorage.deleteImageFromInternal(it)
        }

        dataStore.edit { preferences ->
            preferences[keyIsDiaryTempExist(selectedDate)] = false
            preferences.remove(keyDiaryText(selectedDate))
            preferences.remove(keyDiaryImageUri(selectedDate))
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
