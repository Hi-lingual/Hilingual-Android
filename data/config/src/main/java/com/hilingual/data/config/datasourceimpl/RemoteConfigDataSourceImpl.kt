package com.hilingual.data.config.datasourceimpl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.config.datasource.ConfigRemoteDataSource
import com.hilingual.data.config.model.AppVersion
import com.hilingual.data.config.model.AppVersionInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val KEY_MIN_FORCE_VERSION = "min_force_version"
private const val KEY_LATEST_VERSION = "latest_version"
private const val DEFAULT_VERSION = "0.0.0"

internal class RemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ConfigRemoteDataSource {

    override suspend fun getAppVersionInfo(): AppVersionInfo {
        runCatching {
            remoteConfig.fetchAndActivate().await()
        }.onLogFailure { }

        val minForceVersionStr = remoteConfig.getString(KEY_MIN_FORCE_VERSION)
            .takeIf { it.isNotBlank() } ?: DEFAULT_VERSION
        val latestVersionStr = remoteConfig.getString(KEY_LATEST_VERSION)
            .takeIf { it.isNotBlank() } ?: DEFAULT_VERSION

        return AppVersionInfo(
            minForceVersion = AppVersion(minForceVersionStr),
            latestVersion = AppVersion(latestVersionStr)
        )
    }
}
