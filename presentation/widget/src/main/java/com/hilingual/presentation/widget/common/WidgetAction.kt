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

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.action.Action
import androidx.glance.appwidget.action.actionStartActivity
import com.hilingual.core.common.widget.EXTRA_WIDGET_TYPE
import com.hilingual.core.common.widget.WidgetType

private const val MAIN_ACTIVITY = "com.hilingual.presentation.main.MainActivity"
private const val HOME_DEEP_LINK = "hilingual://app/home"
private const val HOME_ACTION_PREFIX = "com.hilingual.action.OPEN_HOME"

internal fun homeLaunchAction(
    context: Context,
    widgetType: WidgetType,
): Action {
    val intent = Intent("$HOME_ACTION_PREFIX.${widgetType.value}", HOME_DEEP_LINK.toUri())
        .setClassName(context.packageName, MAIN_ACTIVITY)
        .putExtra(EXTRA_WIDGET_TYPE, widgetType.value)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    return actionStartActivity(intent)
}
