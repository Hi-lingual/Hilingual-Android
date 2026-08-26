package com.hilingual.presentation.widget.topic

import com.hilingual.core.designsystem.theme.black
import com.hilingual.core.designsystem.theme.gray100
import com.hilingual.core.designsystem.theme.gray200
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.gray850
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.widget.common.WidgetPreviewTheme
import com.hilingual.presentation.widget.common.widgetColorProvider

internal class RecommendedTopicWidgetColors(previewTheme: WidgetPreviewTheme?) {
    val surface = widgetColorProvider(gray100, gray850, previewTheme)
    val header = widgetColorProvider(gray850, black, previewTheme)
    val onHeader = widgetColorProvider(white, gray200, previewTheme)
    val onHeaderMuted = widgetColorProvider(gray400, gray400, previewTheme)
    val primaryText = widgetColorProvider(black, white, previewTheme)
    val secondaryText = widgetColorProvider(gray500, gray400, previewTheme)
    val accent = widgetColorProvider(hilingualOrange, hilingualOrange, previewTheme)
    val leftHourLargeText = widgetColorProvider(gray200, gray200, previewTheme)
    val leftHourSmallText = widgetColorProvider(gray500, gray200, previewTheme)
}
