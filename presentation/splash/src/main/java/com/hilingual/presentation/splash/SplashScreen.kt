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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.event.LocalSharedTransitionScope
import com.hilingual.core.common.event.LocalSystemBarsColor
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import kotlinx.coroutines.delay
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun SplashRoute(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val localSystemBarsColor = LocalSystemBarsColor.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualOrange
        )
    }

    LaunchedEffect(Unit) {
        delay(1400)
        when (uiState) {
            SplashUiState.LoggedIn -> navigateToHome()
            SplashUiState.NotLoggedIn -> navigateToAuth()
            SplashUiState.OnboardingRequired -> navigateToOnboarding()
        }
    }

    SplashScreen(
        animatedVisibilityScope = animatedVisibilityScope
    )
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
            .background(HilingualTheme.colors.hilingualOrange)
    ) {
        Spacer(
            modifier = Modifier.weight(18f)
        )

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

        Spacer(
            modifier = Modifier.weight(29f)
        )
    }
}
