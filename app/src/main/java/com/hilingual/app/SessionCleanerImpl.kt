package com.hilingual.app

import com.hilingual.core.common.analytics.UserIdentityTracker
import com.hilingual.core.common.app.SessionCleaner
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.data.notification.repository.ReminderRepository
import javax.inject.Inject
import timber.log.Timber

class SessionCleanerImpl @Inject constructor(
    private val userIdentityTracker: UserIdentityTracker,
    private val widgetUpdater: WidgetUpdater,
    private val reminderRepository: ReminderRepository,
) : SessionCleaner {

    override suspend fun clear() {
        userIdentityTracker.clearUserId()
        suspendRunCatching { widgetUpdater.clearCache() }.onFailure(Timber::e)
        reminderRepository.clearReminder().onFailure(Timber::e)
    }
}
