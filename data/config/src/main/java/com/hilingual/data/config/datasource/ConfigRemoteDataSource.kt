package com.hilingual.data.config.datasource

import com.hilingual.data.config.model.AppVersionInfo

interface ConfigRemoteDataSource {
    suspend fun getAppVersionInfo(): AppVersionInfo
}
