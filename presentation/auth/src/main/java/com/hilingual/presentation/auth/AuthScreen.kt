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
package com.hilingual.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectLatestSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.auth.component.GoogleSignButton
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun AuthRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.navigationEvent.collectLatestSideEffect { event ->
        when (event) {
            is AuthSideEffect.NavigateToHome -> navigateToHome()
            is AuthSideEffect.NavigateToSignUp -> navigateToSignUp()
        }
    }

    AuthScreen(
        paddingValues = paddingValues,
        onGoogleSignClick = { viewModel.onGoogleSignClick(context) },
        onPrivacyPolicyClick = { context.launchCustomTabs(UrlConstant.PRIVACY_POLICY) }
    )

    if (isLoading) {
        HilingualLoadingIndicator(
            backgroundColor = HilingualTheme.colors.dim1
        )
    }
}

@Composable
private fun AuthScreen(
    paddingValues: PaddingValues,
    onGoogleSignClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(132f))

        Text(
            text = "일기로 시작하는 일상 속 영어 습관",
            color = HilingualTheme.colors.hilingualBlack,
            style = HilingualTheme.typography.headSB16
        )

        Spacer(Modifier.height(4.dp))

        Image(
            painter = painterResource(R.drawable.img_logo_black),
            contentDescription = null,
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        )

        Spacer(Modifier.height(56.dp))

        Image(
            painter = painterResource(DesignSystemR.drawable.img_login),
            contentDescription = null,
            modifier = Modifier.size(width = 360.dp, height = 245.dp)
        )

        Spacer(Modifier.weight(95f))

        GoogleSignButton(onClick = onGoogleSignClick)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "개인정보처리방침",
            color = HilingualTheme.colors.gray400,
            style = HilingualTheme.typography.bodyR14,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.noRippleClickable(onClick = onPrivacyPolicyClick)
        )
    }
}
