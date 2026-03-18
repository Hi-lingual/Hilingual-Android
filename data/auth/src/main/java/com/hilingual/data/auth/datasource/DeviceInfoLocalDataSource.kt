package com.hilingual.data.auth.datasource

interface DeviceInfoLocalDataSource {
    suspend fun getDeviceUuid(): String
}
