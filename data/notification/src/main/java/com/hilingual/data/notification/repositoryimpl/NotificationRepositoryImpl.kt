package com.hilingual.data.notification.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.notification.datasource.NotificationLocalDataSource
import com.hilingual.data.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationLocalDataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun getIsNotificationDialogShown(): Result<Boolean> =
        suspendRunCatching {
            notificationLocalDataSource.getIsNotificationDialogShown()
        }

    override suspend fun updateIsNotificationDialogShown(isShown: Boolean): Result<Unit> =
        suspendRunCatching {
            notificationLocalDataSource.updateIsNotificationDialogShown(isShown = isShown)
        }
}
