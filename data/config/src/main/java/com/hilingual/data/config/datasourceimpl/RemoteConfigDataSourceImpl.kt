/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
