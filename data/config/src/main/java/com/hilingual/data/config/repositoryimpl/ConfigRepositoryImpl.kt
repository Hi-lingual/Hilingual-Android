package com.hilingual.data.config.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.config.datasource.ConfigRemoteDataSource
import com.hilingual.data.config.model.AppVersionInfo
import com.hilingual.data.config.repository.ConfigRepository
import javax.inject.Inject

internal class ConfigRepositoryImpl @Inject constructor(
    private val configRemoteDataSource: ConfigRemoteDataSource
) : ConfigRepository {
    override suspend fun getAppVersionInfo(): Result<AppVersionInfo> =
        suspendRunCatching {
            configRemoteDataSource.getAppVersionInfo()
        }
}