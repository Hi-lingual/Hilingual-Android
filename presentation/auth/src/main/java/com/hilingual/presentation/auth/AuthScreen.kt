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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectLatestSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.event.LocalSharedTransitionScope
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.presentation.auth.component.GoogleSignButton
import kotlinx.coroutines.delay
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun AuthRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val localSystemBarsColor = LocalSystemBarsColor.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualOrange
        )
    }

    viewModel.navigationEvent.collectLatestSideEffect { event ->
        when (event) {
            is AuthSideEffect.NavigateToHome -> navigateToHome()
            is AuthSideEffect.NavigateToOnboarding -> navigateToOnboarding()
        }
    }

    AuthScreen(
        paddingValues = paddingValues,
        onGoogleSignClick = { viewModel.onGoogleSignClick(context) },
        onPrivacyPolicyClick = { context.launchCustomTabs(UrlConstant.PRIVACY_POLICY) },
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AuthScreen(
    paddingValues: PaddingValues,
    onGoogleSignClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "auth_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.hilingualOrange)
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.47f))

        with(LocalSharedTransitionScope.current) {
            Image(
                painter = painterResource(DesignSystemR.drawable.img_logo),
                contentDescription = null,
                modifier = Modifier.sharedElement(
                    sharedContentState = rememberSharedContentState(key = "logo"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 400)
                    }
                )
            )
        }

        Spacer(Modifier.weight(0.53f))

        Column(
            modifier = Modifier.graphicsLayer { this.alpha = alpha },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(DesignSystemR.drawable.img_login),
                contentDescription = null
            )
            GoogleSignButton(onClick = onGoogleSignClick)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "개인정보처리방침",
                color = HilingualTheme.colors.gray100,
                style = HilingualTheme.typography.bodyM14,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.noRippleClickable(onClick = onPrivacyPolicyClick)
            )
        }
    }
}
