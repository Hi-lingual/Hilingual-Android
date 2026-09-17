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
package com.hilingual.presentation.widget.common

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.collection.intSetOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.setWidgetPreviews
import androidx.glance.appwidget.updateAll
import com.hilingual.core.common.widget.InstalledWidgetCount
import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.data.widget.repository.WidgetRepository
import com.hilingual.presentation.widget.streak.StreakLargeWidgetReceiver
import com.hilingual.presentation.widget.streak.StreakSmallWidgetReceiver
import com.hilingual.presentation.widget.streak.StreakWidget
import com.hilingual.presentation.widget.topic.RecommendedTopicLargeWidgetReceiver
import com.hilingual.presentation.widget.topic.RecommendedTopicSmallWidgetReceiver
import com.hilingual.presentation.widget.topic.RecommendedTopicWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class WidgetUpdaterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val widgetRepository: WidgetRepository,
) : WidgetUpdater {
    override suspend fun clearCache() {
        widgetRepository.clearCache()
    }

    override suspend fun updateAll() {
        RecommendedTopicWidget().updateAll(context)
        StreakWidget().updateAll(context)
        updatePreviews()
    }

    override suspend fun updatePreviews() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) return

        val manager = GlanceAppWidgetManager(context)
        val categories = intSetOf(WIDGET_CATEGORY_HOME_SCREEN)
        manager.setWidgetPreviews<RecommendedTopicSmallWidgetReceiver>(categories)
        manager.setWidgetPreviews<RecommendedTopicLargeWidgetReceiver>(categories)
        manager.setWidgetPreviews<StreakSmallWidgetReceiver>(categories)
        manager.setWidgetPreviews<StreakLargeWidgetReceiver>(categories)
    }

    override fun getInstalledWidgetCount(): InstalledWidgetCount {
        val manager = AppWidgetManager.getInstance(context)
        val diaryTopicCount = manager.getAppWidgetIds(
            ComponentName(context, RecommendedTopicSmallWidgetReceiver::class.java),
        ).size + manager.getAppWidgetIds(
            ComponentName(context, RecommendedTopicLargeWidgetReceiver::class.java),
        ).size
        val streakCount = manager.getAppWidgetIds(
            ComponentName(context, StreakSmallWidgetReceiver::class.java),
        ).size + manager.getAppWidgetIds(
            ComponentName(context, StreakLargeWidgetReceiver::class.java),
        ).size

        return InstalledWidgetCount(
            diaryTopic = diaryTopicCount,
            streak = streakCount,
        )
    }
}
