package com.hilingual.data.notification.datasource

interface NotificationLocalDataSource {
    suspend fun getIsNotificationDialogShown(): Boolean
    suspend fun updateIsNotificationDialogShown(isShown: Boolean)
}
