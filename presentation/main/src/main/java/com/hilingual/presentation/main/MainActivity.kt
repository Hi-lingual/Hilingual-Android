/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.app.AppRestarter
import com.hilingual.core.common.widget.EXTRA_WIDGET_TYPE
import com.hilingual.core.common.widget.WidgetType
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.network.monitor.NetworkMonitor
import com.hilingual.core.notification.HilingualNotificationManager
import com.hilingual.presentation.main.state.rememberMainAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var appRestarter: AppRestarter

    private var pendingDeepLinkUri by mutableStateOf<Uri?>(null)

    /** 푸시 알림으로 진입한 경우에만 채워진다. */
    private var pendingNotificationType by mutableStateOf<String?>(null)

    /** 위젯으로 진입한 경우에만 채워진다. */
    private var pendingWidgetType by mutableStateOf<WidgetType?>(null)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            pendingNotificationType = consumeNotificationType(intent)
            pendingWidgetType = consumeWidgetType(intent)
            pendingDeepLinkUri = consumeDeepLinkUri(intent)
        }

        addOnNewIntentListener { newIntent ->
            consumeNotificationType(newIntent)?.let { pendingNotificationType = it }
            consumeWidgetType(newIntent)?.let { pendingWidgetType = it }
            consumeDeepLinkUri(newIntent)?.let { pendingDeepLinkUri = it }
        }

        setContent {
            HilingualTheme {
                val appState = rememberMainAppState(networkMonitor = networkMonitor)
                MainScreen(
                    appState = appState,
                    tracker = tracker,
                    appRestarter = appRestarter,
                    deepLinkUri = pendingDeepLinkUri,
                    notificationType = pendingNotificationType,
                    widgetType = pendingWidgetType,
                    onDeepLinkConsumed = {
                        pendingDeepLinkUri = null
                        pendingNotificationType = null
                        pendingWidgetType = null
                    },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun consumeDeepLinkUri(intent: Intent?): Uri? {
        val uri = intent?.data ?: intent?.getStringExtra("link")?.let { it.toUri() }
        intent?.data = null
        intent?.removeExtra("link")
        return uri
    }

    private fun consumeNotificationType(intent: Intent?): String? {
        val type = intent?.getStringExtra(HilingualNotificationManager.EXTRA_NOTIFICATION_TYPE)
        intent?.removeExtra(HilingualNotificationManager.EXTRA_NOTIFICATION_TYPE)
        return type
    }

    private fun consumeWidgetType(intent: Intent?): WidgetType? {
        val type = WidgetType.from(intent?.getStringExtra(EXTRA_WIDGET_TYPE))
        intent?.removeExtra(EXTRA_WIDGET_TYPE)
        return type
    }
}
