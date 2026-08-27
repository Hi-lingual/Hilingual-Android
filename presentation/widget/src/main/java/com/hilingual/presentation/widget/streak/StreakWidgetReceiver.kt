package com.hilingual.presentation.widget.streak

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.hilingual.presentation.widget.common.LargeWidgetPreviewSize
import com.hilingual.presentation.widget.common.SmallWidgetPreviewSize

class StreakSmallWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidget(SmallWidgetPreviewSize)
}

class StreakLargeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidget(LargeWidgetPreviewSize)
}
