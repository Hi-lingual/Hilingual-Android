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
package com.hilingual.core.ads.initializer

import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig.Builder
import com.hilingual.core.ads.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

@Singleton
internal class GmaAdsInitializer @Inject constructor() : AdsInitializer {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun initialize(context: Context) {
        applicationScope.launch {
            try {
                val initConfig = Builder(BuildConfig.ADMOB_APP_ID).build()
                MobileAds.initialize(context, initConfig) { status ->
                    Timber.tag("GMA").d("GMA Next Gen SDK 초기화 완료: %s", status)
                }
            } catch (e: Exception) {
                Timber.tag("GMA").e(e, "GMA Next Gen SDK 초기화 실패")
            }
        }
    }
}
