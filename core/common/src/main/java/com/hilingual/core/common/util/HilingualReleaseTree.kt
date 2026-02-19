package com.hilingual.core.common.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class HilingualReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val isLoggable = priority >= Log.WARN
        if (isLoggable) {
            val crashlytics = FirebaseCrashlytics.getInstance()

            val logMessage = if (tag != null) "[$tag] $message" else message
            crashlytics.log(logMessage)

            if (t != null) crashlytics.recordException(t)
        }
    }
}
