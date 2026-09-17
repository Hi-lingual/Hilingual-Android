package com.hilingual.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hilingual.core.notification.HilingualNotificationManager
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class HilingualFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: HilingualNotificationManager

    @Inject
    lateinit var userRepository: UserRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: return@let
            val body = notification.body ?: return@let
            val notificationType = remoteMessage.data["notification_type"]
            val deepLink = remoteMessage.data["link"]

            notificationManager.sendReminderNotification(
                notificationType = notificationType,
                title = title,
                message = body,
                deepLink = deepLink,
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM Token: $token")
        userRepository.scheduleFcmTokenSync(fcmToken = token)
    }
}
