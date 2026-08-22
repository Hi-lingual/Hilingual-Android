package com.hilingual.presentation.widget.common

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.presentation.widget.streak.StreakWidget
import com.hilingual.presentation.widget.topic.RecommendedTopicWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class WidgetUpdaterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : WidgetUpdater {
    override suspend fun updateAll() {
        RecommendedTopicWidget().updateAll(context)
        StreakWidget().updateAll(context)
    }
}
