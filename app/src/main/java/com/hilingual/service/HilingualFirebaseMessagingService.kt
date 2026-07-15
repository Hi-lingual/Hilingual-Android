/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        Timber.tag("FCM_TOKEN").d("FCM 수신됨: ${remoteMessage.data}")

        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: return@let
            val body = notification.body ?: return@let
            val channelId = remoteMessage.data["channelId"]
            val deepLink = remoteMessage.data["link"]

            notificationManager.sendReminderNotification(
                channelId = channelId,
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
