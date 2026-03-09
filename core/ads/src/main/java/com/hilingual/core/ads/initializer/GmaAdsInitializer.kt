package com.hilingual.core.ads.initializer

import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig.Builder
import com.hilingual.core.ads.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GmaAdsInitializer @Inject constructor() : AdsInitializer {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun initialize(context: Context) {
        applicationScope.launch {
            try {
                val initConfig = Builder(BuildConfig.ADMOB_APP_ID).build()
                MobileAds.initialize(context, initConfig) { status ->
                    Timber.d("GMA Next Gen SDK 초기화 완료: %s", status)
                }
            } catch (e: Exception) {
                Timber.e(e, "GMA Next Gen SDK 초기화 실패")
            }
        }
    }
}
