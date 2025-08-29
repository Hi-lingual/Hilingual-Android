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
package com.hilingual.presentation.otp

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.component.topappbar.TitleCenterAlignedTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.otp.component.OtpTextField

@Composable
fun OtpRoute(
    paddingValues: PaddingValues,
    navigateToOnboarding: () -> Unit
) {
    val context = LocalContext.current
    var otpCode by remember { mutableStateOf("") }
    var authFailureCount by remember { mutableIntStateOf(0) }
    var isOtpInvalid by remember { mutableStateOf(false) }

    OtpScreen(
        paddingValues = paddingValues,
        otpCodeProvider = { otpCode },
        onOtpCodeChange = {
            isOtpInvalid = false
            otpCode = it
        },
        isOtpInvalidProvider = { isOtpInvalid },
        authFailureCountProvider = { authFailureCount },
        onNotReceivedCodeClick = { context.launchCustomTabs(UrlConstant.KAKAOTALK_CHANNEL) },
        onAuthClick = {
            if (otpCode == "123456") {
                navigateToOnboarding()
            } else {
                isOtpInvalid = true
                authFailureCount++
            }
        },
        onContactClick = { context.launchCustomTabs(UrlConstant.KAKAOTALK_CHANNEL) },
        onExitClick = { (context as? Activity)?.finishAffinity() }
    )
}

@Composable
private fun OtpScreen(
    paddingValues: PaddingValues,
    otpCodeProvider: () -> String,
    onOtpCodeChange: (String) -> Unit,
    isOtpInvalidProvider: () -> Boolean,
    authFailureCountProvider: () -> Int,
    onNotReceivedCodeClick: () -> Unit,
    onAuthClick: () -> Unit,
    onContactClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isOtpInvalid = isOtpInvalidProvider()

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleCenterAlignedTopAppBar(title = "인증 번호 입력")

        Spacer(Modifier.height(32.dp))

        OtpTextField(
            otpText = otpCodeProvider,
            onOtpTextChange = onOtpCodeChange,
            isError = isOtpInvalidProvider()
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (isOtpInvalid) "유효하지 않은 인증코드입니다. [실패 횟수 ${authFailureCountProvider()}/5]" else "",
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.alertRed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HilingualTheme.colors.gray100)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Q. 인증 번호란?",
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.black
            )
            Text(
                text = "사전 예약을 신청한 분들을 대상으로 가입 인증 번호가 발급되었어요. 인증 번호를 보유하신 경우에만 가입이 가능해요.\n\n" +
                    "알림을 신청한 이메일을 확인해주세요.",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray500
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "인증 번호가 오지 않았나요?",
            style = HilingualTheme.typography.bodyM14,
            textDecoration = TextDecoration.Underline,
            color = HilingualTheme.colors.gray500,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .noRippleClickable(onClick = onNotReceivedCodeClick)
        )

        Spacer(Modifier.weight(1f))

        HilingualButton(
            text = "인증하기",
            onClick = onAuthClick,
            enableProvider = { otpCodeProvider().length == 6 },
            modifier = Modifier.padding(16.dp)
        )
    }

    OtpFailureDialog(
        isVisible = authFailureCountProvider() >= 5,
        onContactClick = onContactClick,
        onExitClick = onExitClick
    )
}

@Composable
private fun OtpFailureDialog(
    isVisible: Boolean,
    onExitClick: () -> Unit,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "인증에 실패했어요.",
            description = "사전 예약 알림신청을 통해 발급한\n" +
                "인증코드가 맞는지 다시 한번 확인해주세요.",
            cancelText = "앱 종료",
            confirmText = "문의하기",
            onNegative = onExitClick,
            onPositive = onContactClick,
            onDismiss = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}
