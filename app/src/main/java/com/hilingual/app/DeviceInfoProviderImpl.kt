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
package com.hilingual.app

import android.content.Context
import android.os.Build
import com.hilingual.core.common.extension.appVersionName
import com.hilingual.core.common.app.DeviceInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class DeviceInfoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceInfoProvider {

    override fun getDeviceName(): String = Build.MODEL

    override fun getDeviceType(): String =
        if (context.resources.configuration.smallestScreenWidthDp >= 600) "TABLET" else "PHONE"

    override fun getOsVersion(): String = Build.VERSION.RELEASE

    override fun getAppVersion(): String = context.appVersionName

    override fun getProvider(): String = "GOOGLE"

    override fun getRole(): String = "USER"

    override fun getOsType(): String = "Android"
}
