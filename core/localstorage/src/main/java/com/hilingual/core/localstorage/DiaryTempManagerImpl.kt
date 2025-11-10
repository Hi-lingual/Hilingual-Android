package com.hilingual.core.localstorage

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class DiaryTempManagerImpl(
    private val dataStore: DataStore<Preferences>
) : DiaryTempManager {

    private fun keyHasDiaryTemp(date: LocalDate) = booleanPreferencesKey("diary_${date}_has_temp")
    private fun keyDiaryText(date: LocalDate) = stringPreferencesKey("diary_${date}_text")
    private fun keyDiaryImageUri(date: LocalDate) = stringPreferencesKey("diary_${date}_image_url")

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

                if (imageUri != null) {
                    preferences[keyDiaryImageUri(selectedDate)] = imageUri.toString()
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
        dataStore.edit { preferences ->
            preferences[keyHasDiaryTemp(selectedDate)] = false
            preferences.remove(keyDiaryText(selectedDate))
            preferences.remove(keyDiaryImageUri(selectedDate))
        }
    }
}
