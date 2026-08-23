package com.hilingual.presentation.widget.common

import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN
import android.content.Context
import android.os.Build
import androidx.collection.intSetOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.setWidgetPreviews
import androidx.glance.appwidget.updateAll
import com.hilingual.core.common.widget.WidgetUpdater
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
) : WidgetUpdater {
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
}
