/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.angrypodo.wisp.runtime.Wisp
import com.hilingual.core.ads.initializer.AdsInitializer
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.util.HilingualReleaseTree
import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.core.notification.HilingualNotificationManager
import com.hilingual.core.work.scheduler.HilingualWorkManagerConfigurator
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var imageLoader: Lazy<ImageLoader>

    @Inject
    lateinit var workConfigurator: HilingualWorkManagerConfigurator

    @Inject
    lateinit var adsInitializer: AdsInitializer

    @Inject
    lateinit var notificationManager: HilingualNotificationManager

    @Inject
    lateinit var widgetUpdater: WidgetUpdater

    @Inject
    lateinit var tracker: Tracker

    override fun onCreate() {
        super.onCreate()
        SingletonImageLoader.setSafe { imageLoader.get() }

        setDayMode()
        initTimber()
        initWorkManager()
        initAds()
        initNotificationChannels()
        syncWidgetCount()
        updateWidgets()
        Wisp.initialize()
    }

    override fun newImageLoader(context: Context): ImageLoader = imageLoader.get()

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initTimber() {
        val tree = if (BuildConfig.DEBUG) Timber.DebugTree() else HilingualReleaseTree()
        Timber.plant(tree)
    }

    private fun initWorkManager() {
        workConfigurator.initialize()
    }

    private fun initAds() {
        adsInitializer.initialize(this)
    }

    private fun initNotificationChannels() {
        notificationManager.createNotificationChannels()
    }

    private fun updateWidgets() {
        applicationScope.launch {
            runCatching { widgetUpdater.updateAll() }
                .onFailure(Timber::e)
        }
    }

    private fun syncWidgetCount() {
        val count = widgetUpdater.getInstalledWidgetCount()
        tracker.logGlobalAction(
            trigger = TriggerType.NONE,
            action = "widget_count",
            properties = mapOf(
                "widget_count_diary_topic" to count.diaryTopic,
                "widget_count_streak" to count.streak,
                "widget_count_total" to count.total,
            ),
        )
    }
}
