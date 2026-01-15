package com.hilingual.data.config.repository

import com.hilingual.data.config.model.AppVersionInfo

interface ConfigRepository {
    suspend fun getAppVersionInfo(): Result<AppVersionInfo>
}
