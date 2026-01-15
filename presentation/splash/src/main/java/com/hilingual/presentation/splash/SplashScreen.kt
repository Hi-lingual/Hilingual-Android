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
package com.hilingual.presentation.splash

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectLatestSideEffect
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.provider.LocalSharedTransitionScope
import com.hilingual.core.designsystem.component.dialog.OneButtonDialog
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.data.config.model.UpdateState
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun SplashRoute(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current

    viewModel.sideEffect.collectLatestSideEffect { event ->
        when (event) {
            is SplashSideEffect.NavigateToHome -> navigateToHome()
            is SplashSideEffect.NavigateToAuth -> navigateToAuth()
            is SplashSideEffect.NavigateToStore -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "market://details?id=${context.packageName}".toUri()
                    setPackage("com.android.vending")
                }
                context.startActivity(intent)
            }
        }
    }

    SplashScreen(
        animatedVisibilityScope = animatedVisibilityScope
    )

    when (uiState.updateState) {
        UpdateState.FORCE -> {
            ForceDialog(
                onConfirm = viewModel::onUpdateConfirm,
                onDismiss = { activity?.finish() }
            )
        }

        UpdateState.OPTIONAL -> {
            OptionalDialog(
                onPositive = viewModel::onUpdateConfirm,
                onNegative = viewModel::onUpdateSkip,
                onDismiss = viewModel::onUpdateSkip
            )
        }

        UpdateState.NONE -> {}
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SplashScreen(
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarColor(hilingualOrange)
            .background(HilingualTheme.colors.hilingualOrange)
    ) {
        Spacer(
            modifier = Modifier.weight(18f)
        )

        with(LocalSharedTransitionScope.current) {
            Image(
                painter = painterResource(DesignSystemR.drawable.img_logo),
                contentDescription = null,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "logo"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 400)
                        }
                    )
                    .size(width = 200.dp, height = 50.dp)
            )
        }

        Spacer(
            modifier = Modifier.weight(29f)
        )
    }
}

@Composable
private fun ForceDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    OneButtonDialog(
        confirmText = "업데이트 하기",
        onConfirm = onConfirm,
        modifier = modifier,
        onDismiss = onDismiss,
        content = {
            Text(
                text = "최신 버전의 앱이 출시 되었습니다",
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.gray850
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "안정적인 서비스 사용을 위해\n" +
                    "최신 버전의 앱으로 업데이트 해주세요.",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray400,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
private fun OptionalDialog(
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    TwoButtonDialog(
        title = "최신 버전의 앱이 출시 되었습니다",
        description = "지금 바로 업데이트 하고 \n" +
            "최적의 사용 환경을 이용해보세요.",
        cancelText = "다음에 하기",
        confirmText = "업데이트 하기",
        onPositive = onPositive,
        onNegative = onNegative,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ForceDialogPreview() {
    HilingualTheme {
        ForceDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun OptionalDialogPreview() {
    HilingualTheme {
        OptionalDialog(
            onPositive = {},
            onNegative = {},
            onDismiss = {}
        )
    }
}
