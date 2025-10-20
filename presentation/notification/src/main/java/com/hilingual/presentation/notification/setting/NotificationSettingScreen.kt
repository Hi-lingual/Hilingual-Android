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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.setting.component.NotificationSwitchItem

@Composable
internal fun NotificationSettingRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: NotificationSettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            HilingualLoadingIndicator()
        }
        is UiState.Success -> {
            NotificationSettingScreen(
                isMarketingChecked = state.data.isMarketingChecked,
                onMarketingCheckedChange = viewModel::updateMarketingChecked,
                isFeedChecked = state.data.isFeedChecked,
                onFeedCheckedChange = viewModel::updateFeedChecked,
                paddingValues = paddingValues,
                onBackClick = navigateUp
            )
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
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "알림 설정",
            onBackClicked = onBackClick
        )

        NotificationSwitchItem(
            text = "마케팅 알림",
            isChecked = isMarketingChecked,
            onCheckedChange = onMarketingCheckedChange
        )

        NotificationSwitchItem(
            text = "피드 알림",
            isChecked = isFeedChecked,
            onCheckedChange = onFeedCheckedChange
        )
    }
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
            paddingValues = PaddingValues(0.dp),
            onBackClick = {}
        )
    }
}
