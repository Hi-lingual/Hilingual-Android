package com.hilingual.presentation.auth

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.auth.component.GoogleSignButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun AuthRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val localSystemBarsColor = LocalSystemBarsColor.current

    DisposableEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualOrange
        )

        onDispose {
            localSystemBarsColor.setSystemBarColor(
                systemBarsColor = white
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is AuthSideEffect.NavigateToHome -> navigateToHome()
                is AuthSideEffect.NavigateToOnboarding -> navigateToOnboarding()
            }
        }
    }

    AuthScreen(
        paddingValues = paddingValues,
        onGoogleSignClick = viewModel::onGoogleSignClick,
        onPrivacyPolicyClick = { },
        sharedTransitionScope = sharedTransitionScope,
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
    sharedTransitionScope: SharedTransitionScope,
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

        Spacer(Modifier.weight(0.53f))

        Column(
            modifier = Modifier.graphicsLayer { this.alpha = alpha },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.img_login),
                contentDescription = null,
            )
            GoogleSignButton(onClick = onGoogleSignClick)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "개인정보처리방침",
                color = HilingualTheme.colors.gray100,
                style = HilingualTheme.typography.bodyM14,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.noRippleClickable(onClick = onPrivacyPolicyClick),
            )
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    HilingualTheme {
//        AuthScreen(
//            paddingValues = PaddingValues(0.dp),
//            onGoogleSignClick = {},
//            onPrivacyPolicyClick = {}
//        )
    }
}
