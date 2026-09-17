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
