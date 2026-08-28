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
