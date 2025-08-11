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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.otp.component.OtpTextField

@Composable
fun OtpRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOnboarding: () -> Unit
) {
    OtpScreen(
        paddingValues = paddingValues,
        onBackClicked = navigateUp,
        onTextClick = {},
        onButtonClick = navigateToOnboarding,
        isButtonEnable = { true }
    )
}

@Composable
private fun OtpScreen(
    paddingValues: PaddingValues,
    onBackClicked: () -> Unit,
    onTextClick: () -> Unit,
    onButtonClick: () -> Unit,
    isButtonEnable: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackTopAppBar(
            title = "인증 번호 입력",
            onBackClicked = onBackClicked
        )

        Spacer(Modifier.height(32.dp))

        OtpTextField(
            otpText = { value },
            onOtpTextChange = { value = it }
        )

        Spacer(Modifier.height(40.dp))

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
                .noRippleClickable(onClick = onTextClick)
        )

        Spacer(Modifier.weight(1f))

        HilingualButton(
            text = "인증하기",
            onClick = onButtonClick,
            enableProvider = isButtonEnable,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun OtpScreenPreview() {
    HilingualTheme {
        OtpScreen(
            paddingValues = PaddingValues(0.dp),
            onBackClicked = {},
            onTextClick = {},
            onButtonClick = {},
            isButtonEnable = { true }
        )
    }
}
