package com.hilingual.presentation.splash

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SplashRoute(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
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

    LaunchedEffect(uiState) {
        delay(1400)
        when (uiState) {
            SplashUiState.LoggedIn -> navigateToHome()
            SplashUiState.NotLoggedIn -> navigateToAuth()
            SplashUiState.OnboardingRequired -> navigateToOnboarding()
        }
    }

    SplashScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SplashScreen(
    sharedTransitionScope: SharedTransitionScope,
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

        with(sharedTransitionScope) {
            Image(
                painter = painterResource(R.drawable.img_logo),
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
