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
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.app.AppRestarter
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.network.monitor.NetworkMonitor
import com.hilingual.presentation.main.state.rememberMainAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var appRestarter: AppRestarter

    private val deepLinkUri = MutableSharedFlow<Uri>(extraBufferCapacity = 1)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        val initialDeepLink = consumeDeepLinkUri(intent)

        setContent {
            HilingualTheme {
                val appState = rememberMainAppState(networkMonitor = networkMonitor)
                MainScreen(
                    appState = appState,
                    tracker = tracker,
                    appRestarter = appRestarter,
                    initialDeepLinkUri = initialDeepLink,
                    deepLinkUri = deepLinkUri,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        consumeDeepLinkUri(intent)?.let { deepLinkUri.tryEmit(it) }
    }

    private fun consumeDeepLinkUri(intent: Intent?): Uri? {
        val uri = intent?.data ?: intent?.getStringExtra("link")?.let { Uri.parse(it) }
        intent?.data = null
        intent?.removeExtra("link")
        return uri
    }
}
