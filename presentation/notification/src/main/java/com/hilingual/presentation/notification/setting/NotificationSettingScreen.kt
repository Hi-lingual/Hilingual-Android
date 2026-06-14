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
package com.hilingual.presentation.notification.setting

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.util.HandleLoadError
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.presentation.notification.setting.component.NotificationSettingBanner
import com.hilingual.presentation.notification.setting.component.NotificationSettingDialog
import com.hilingual.presentation.notification.setting.component.NotificationSwitchItem

@Composable
internal fun NotificationSettingRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: NotificationSettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isNotificationGranted by viewModel.isNotificationGranted.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var isNotificationSettingDialogVisible by remember { mutableStateOf(false) }

    HandleLoadError(
        uiState = uiState,
        onActionClick = viewModel::getNotificationSettings,
    )

    fun checkNotificationPermission() {
        val isGranted = NotificationManagerCompat.from(context).areNotificationsEnabled()
        viewModel.checkNotificationPermission(isGranted)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        checkNotificationPermission()
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            NotificationSettingSideEffect.ShowPermissionDialog -> {
                isNotificationSettingDialogVisible = true
            }
        }
    }

    val navigateToSettings = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            HilingualLoadingIndicator()
        }

        is UiState.Success -> {
            isNotificationGranted?.let { granted ->
                NotificationSettingScreen(
                    isMarketingChecked = state.data.isMarketingChecked,
                    onMarketingCheckedChange = viewModel::updateMarketingChecked,
                    isFeedChecked = state.data.isFeedChecked,
                    onFeedCheckedChange = viewModel::updateFeedChecked,
                    isNotificationGranted = granted,
                    onBannerClick = navigateToSettings,
                    isPermissionDialogVisible = isNotificationSettingDialogVisible,
                    onPermissionDialogDismiss = { isNotificationSettingDialogVisible = false },
                    onPermissionDialogConfirm = {
                        isNotificationSettingDialogVisible = false
                        navigateToSettings()
                    },
                    paddingValues = paddingValues,
                    onBackClick = navigateUp,
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun NotificationSettingScreen(
    isMarketingChecked: Boolean,
    onMarketingCheckedChange: (Boolean) -> Unit,
    isFeedChecked: Boolean,
    onFeedCheckedChange: (Boolean) -> Unit,
    isNotificationGranted: Boolean,
    onBannerClick: () -> Unit,
    isPermissionDialogVisible: Boolean,
    onPermissionDialogDismiss: () -> Unit,
    onPermissionDialogConfirm: () -> Unit,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
    ) {
        BackTopAppBar(
            title = "알림 설정",
            onBackClicked = onBackClick,
        )

        NotificationSettingBanner(
            isVisible = !isNotificationGranted,
            onClick = onBannerClick,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        )

        NotificationSwitchItem(
            text = "마케팅 알림",
            isChecked = isMarketingChecked,
            onCheckedChange = onMarketingCheckedChange,
        )

        NotificationSwitchItem(
            text = "피드 알림",
            isChecked = isFeedChecked,
            onCheckedChange = onFeedCheckedChange,
        )
    }

    NotificationSettingDialog(
        isVisible = isPermissionDialogVisible,
        onDismiss = onPermissionDialogDismiss,
        onConfirmClick = onPermissionDialogConfirm,
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationSettingScreenPreview() {
    var isMarketingChecked by remember { mutableStateOf(true) }
    var isFeedChecked by remember { mutableStateOf(false) }

    HilingualTheme {
        NotificationSettingScreen(
            isMarketingChecked = isMarketingChecked,
            onMarketingCheckedChange = { isMarketingChecked = it },
            isFeedChecked = isFeedChecked,
            onFeedCheckedChange = { isFeedChecked = it },
            isNotificationGranted = false,
            onBannerClick = {},
            paddingValues = PaddingValues(0.dp),
            onBackClick = {},
            isPermissionDialogVisible = false,
            onPermissionDialogDismiss = {},
            onPermissionDialogConfirm = {},
        )
    }
}
