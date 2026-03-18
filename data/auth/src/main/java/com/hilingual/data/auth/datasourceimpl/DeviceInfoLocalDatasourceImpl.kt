package com.hilingual.data.auth.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hilingual.data.auth.datasource.DeviceInfoLocalDataSource
import com.hilingual.data.auth.di.qualifier.DeviceInfoDataStore
import jakarta.inject.Inject
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DeviceInfoLocalDatasourceImpl @Inject constructor(
    @DeviceInfoDataStore private val deviceInfoDataStore: DataStore<Preferences>,
) : DeviceInfoLocalDataSource {
    private object DeviceInfoDataStoreKey {
        val DEVICE_UUID = stringPreferencesKey("device_uuid")
    }

    override suspend fun getDeviceUuid(): String {
        val uuid = deviceInfoDataStore.data
            .map { it[DeviceInfoDataStoreKey.DEVICE_UUID] }
            .first()

        if (uuid != null) return uuid

        return UUID.randomUUID().toString().also { saveDeviceUuid(it) }
    }

    private suspend fun saveDeviceUuid(uuid: String) {
        deviceInfoDataStore.edit { preferences ->
            preferences[DeviceInfoDataStoreKey.DEVICE_UUID] = uuid
        }
    }
}
