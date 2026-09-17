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
package com.hilingual.presentation.widget.streak

import com.hilingual.core.designsystem.theme.black
import com.hilingual.core.designsystem.theme.gray100
import com.hilingual.core.designsystem.theme.gray200
import com.hilingual.core.designsystem.theme.gray300
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.gray700
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.widget.common.WidgetPreviewTheme
import com.hilingual.presentation.widget.common.widgetColorProvider

internal class StreakWidgetColors(previewTheme: WidgetPreviewTheme?) {
    val surface = widgetColorProvider(gray100, gray700, previewTheme)
    val primaryText = widgetColorProvider(black, white, previewTheme)
    val secondaryText = widgetColorProvider(gray500, gray200, previewTheme)
    val lockedBar = widgetColorProvider(gray300, gray500, previewTheme)
    val inactiveDay = widgetColorProvider(gray200, gray500, previewTheme)
    val inactiveFire = widgetColorProvider(gray400, gray300, previewTheme)
    val onActiveDay = widgetColorProvider(white, white, previewTheme)
}
