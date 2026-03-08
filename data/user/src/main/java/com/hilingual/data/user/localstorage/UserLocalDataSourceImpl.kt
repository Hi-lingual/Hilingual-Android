package com.hilingual.data.user.localstorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserLocalDataSource {

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_REGISTER_COMPLETED] = isCompleted
        }
    }

    override suspend fun getRegisterStatus(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[KEY_IS_REGISTER_COMPLETED] ?: false
        }.first()
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        val KEY_IS_REGISTER_COMPLETED = booleanPreferencesKey("is_register_completed")
    }
}
