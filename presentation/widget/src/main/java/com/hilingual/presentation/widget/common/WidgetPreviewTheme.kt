package com.hilingual.presentation.widget.common

import androidx.compose.ui.graphics.Color
import androidx.glance.color.ColorProvider
import androidx.glance.unit.ColorProvider as GlanceColorProvider

internal enum class WidgetPreviewTheme {
    LIGHT,
    DARK,
}

internal fun widgetColorProvider(
    day: Color,
    night: Color,
    previewTheme: WidgetPreviewTheme?,
): GlanceColorProvider = when (previewTheme) {
    WidgetPreviewTheme.LIGHT -> ColorProvider(day = day, night = day)
    WidgetPreviewTheme.DARK -> ColorProvider(day = night, night = night)
    null -> ColorProvider(day = day, night = night)
}
