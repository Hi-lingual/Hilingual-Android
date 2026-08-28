package com.hilingual.presentation.widget.common

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview

internal val SmallWidgetPreviewSize = DpSize(155.dp, 155.dp)
internal val LargeWidgetPreviewSize = DpSize(329.dp, 155.dp)

@OptIn(ExperimentalGlancePreviewApi::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(widthDp = 329, heightDp = 155)
@Preview(widthDp = 155, heightDp = 155)
internal annotation class WidgetPreviews
