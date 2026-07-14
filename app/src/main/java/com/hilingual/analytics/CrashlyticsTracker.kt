package com.hilingual.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.hilingual.core.common.analytics.UserIdentityTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsTracker @Inject constructor() : UserIdentityTracker {

    private val crashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    override fun setUserId(userId: Long) {
        crashlytics.setUserId(userId.toString())
    }

    override fun clearUserId() {
        crashlytics.setUserId("")
    }
}
