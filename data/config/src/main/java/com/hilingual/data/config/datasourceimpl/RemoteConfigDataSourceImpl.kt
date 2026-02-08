package com.hilingual.data.config.datasourceimpl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hilingual.core.common.constant.STABLE_VERSION
import com.hilingual.data.config.constant.KEY_LATEST_VERSION
import com.hilingual.data.config.constant.KEY_MIN_FORCE_VERSION
import com.hilingual.data.config.datasource.ConfigRemoteDataSource
import com.hilingual.data.config.model.AppVersion
import com.hilingual.data.config.model.AppVersionInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ConfigRemoteDataSource {

    override suspend fun getAppVersionInfo(): AppVersionInfo {
        remoteConfig.fetchAndActivate().await()

        val minForceVersionStr = remoteConfig.getString(KEY_MIN_FORCE_VERSION)
            .takeIf { it.isNotBlank() } ?: STABLE_VERSION
        val latestVersionStr = remoteConfig.getString(KEY_LATEST_VERSION)
            .takeIf { it.isNotBlank() } ?: STABLE_VERSION

        return AppVersionInfo(
            minForceVersion = AppVersion(minForceVersionStr),
            latestVersion = AppVersion(latestVersionStr)
        )
    }
}
