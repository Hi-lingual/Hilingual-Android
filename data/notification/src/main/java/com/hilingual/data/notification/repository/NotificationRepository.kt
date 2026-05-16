package com.hilingual.data.notification.repository

interface NotificationRepository {
    suspend fun getIsNotificationDialogShown(): Result<Boolean>
    suspend fun updateIsNotificationDialogShown(isShown: Boolean): Result<Unit>
}
