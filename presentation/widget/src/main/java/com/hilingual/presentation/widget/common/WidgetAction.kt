package com.hilingual.presentation.widget.common

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.action.Action
import androidx.glance.appwidget.action.actionStartActivity

private const val MAIN_ACTIVITY = "com.hilingual.presentation.main.MainActivity"
private const val HOME_DEEP_LINK = "hilingual://app/home"

internal fun homeLaunchAction(context: Context): Action {
    val intent = Intent(Intent.ACTION_VIEW, HOME_DEEP_LINK.toUri())
        .setClassName(context.packageName, MAIN_ACTIVITY)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    return actionStartActivity(intent)
}
