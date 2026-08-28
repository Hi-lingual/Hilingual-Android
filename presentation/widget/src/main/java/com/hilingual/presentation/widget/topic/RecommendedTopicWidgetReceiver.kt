package com.hilingual.presentation.widget.topic

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.hilingual.presentation.widget.common.LargeWidgetPreviewSize
import com.hilingual.presentation.widget.common.SmallWidgetPreviewSize

class RecommendedTopicSmallWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RecommendedTopicWidget(SmallWidgetPreviewSize)
}

class RecommendedTopicLargeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RecommendedTopicWidget(LargeWidgetPreviewSize)
}
