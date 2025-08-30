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
package com.hilingual.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.topappbar.TitleLeftAlignedTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.mypage.component.LogoutDialog
import com.hilingual.presentation.mypage.component.MyInfoBox
import com.hilingual.presentation.mypage.component.SettingItem

@Composable
internal fun MyPageRoute(
    paddingValues: PaddingValues,
    navigateToProfileEdit: () -> Unit,
    navigateToMyFeedProfile: () -> Unit,
    navigateToAlarm: () -> Unit,
    navigateToBlock: () -> Unit,
    navigateToSplash: () -> Unit,
    viewModel: MyPageViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MyPageSideEffect.ShowRetryDialog -> {
                dialogTrigger.show(sideEffect.onRetry)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Success -> {
            MyPageScreen(
                paddingValues = paddingValues,
                profileImageUrl = state.data.profileImageUrl,
                profileNickname = state.data.profileNickname,
                onProfileEditClick = navigateToProfileEdit,
                onMyFeedClick = navigateToMyFeedProfile,
                onAlarmClick = navigateToAlarm,
                onBlockClick = navigateToBlock,
                onCustomerCenterClick = { context.launchCustomTabs(UrlConstant.KAKAOTALK_CHANNEL) },
                onTermsClick = { context.launchCustomTabs(UrlConstant.PRIVACY_POLICY) },
                onLogoutClick = navigateToSplash
            )
        }

        else -> {}
    }
}

@Composable
private fun MyPageScreen(
    paddingValues: PaddingValues,
    profileImageUrl: String,
    profileNickname: String,
    onProfileEditClick: () -> Unit,
    onMyFeedClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onBlockClick: () -> Unit,
    onCustomerCenterClick: () -> Unit,
    onTermsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var isLogoutDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.gray100)
            .background(HilingualTheme.colors.gray100)
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleLeftAlignedTopAppBar(
            title = "마이페이지",
            textColor = HilingualTheme.colors.black
        )

        MyInfoBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            profileUrl = profileImageUrl,
            profileNickname = profileNickname,
            onEditButtonClick = onProfileEditClick,
            onMyFeedButtonClick = onMyFeedClick
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(HilingualTheme.colors.white)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingItem(
                iconRes = R.drawable.ic_alarm_28,
                title = "알림 설정",
                onClick = onAlarmClick,
                trailingContent = { ArrowIcon() }
            )

            SettingItem(
                iconRes = R.drawable.ic_block_24_black,
                title = "차단한 유저",
                onClick = onBlockClick,
                trailingContent = { ArrowIcon() }
            )

            SettingItem(
                iconRes = R.drawable.ic_customer_24,
                title = "고객센터",
                onClick = onCustomerCenterClick,
                trailingContent = { ArrowIcon() }
            )

            SettingItem(
                iconRes = R.drawable.ic_document_24,
                title = "개인정보 처리방침 및 이용약관",
                onClick = onTermsClick,
                trailingContent = { ArrowIcon() }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingItem(
                iconRes = R.drawable.ic_info_24,
                title = "버전 정보",
                trailingContent = {
                    Text(
                        text = "1.01.01", // TODO: 버전 정보 가져오기 by 지영
                        color = HilingualTheme.colors.gray400,
                        style = HilingualTheme.typography.captionR14,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            )

            SettingItem(
                iconRes = R.drawable.ic_logout_24,
                title = "로그아웃",
                onClick = { isLogoutDialogVisible = true }
            )
        }
    }

    LogoutDialog(
        isVisible = isLogoutDialogVisible,
        onDismiss = { isLogoutDialogVisible = false },
        onLogoutClick = onLogoutClick
    )
}

@Composable
private fun ArrowIcon() {
    Icon(
        modifier = Modifier
            .size(24.dp)
            .padding(4.dp),
        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_16_bold),
        contentDescription = null,
        tint = HilingualTheme.colors.gray400
    )
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    HilingualTheme {
        MyPageScreen(
            paddingValues = PaddingValues(),
            profileImageUrl = "",
            profileNickname = "하링이",
            onProfileEditClick = {},
            onMyFeedClick = {},
            onAlarmClick = {},
            onBlockClick = {},
            onCustomerCenterClick = {},
            onTermsClick = {},
            onLogoutClick = {}
        )
    }
}
