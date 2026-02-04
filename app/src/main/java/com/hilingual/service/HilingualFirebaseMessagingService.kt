package com.hilingual.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hilingual.core.notification.HilingualNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HilingualFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: HilingualNotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: return@let
            val body = notification.body ?: return@let
            val channelId = remoteMessage.data["channelId"]

            notificationManager.sendReminderNotification(channelId, title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM Token: $token")
        // TODO: 추후 WorkManager를 통해 서버로 토큰 전송 로직 구현 예정
    }
}
