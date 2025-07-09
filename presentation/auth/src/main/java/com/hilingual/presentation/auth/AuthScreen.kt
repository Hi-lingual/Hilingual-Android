package com.hilingual.presentation.auth

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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.presentation.auth.component.GoogleSignButton

@Composable
internal fun AuthRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(color = hilingualOrange, darkIcons = false)
    }

    // TODO: SideEffect로 navigateToHome, navigateToOnboarding 처리 by.민재
    AuthScreen(
        paddingValues = paddingValues,
        onGoogleSignClick = navigateToOnboarding, // 임시
        onPrivacyPolicyClick = { }
    )
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
            .background(HilingualTheme.colors.hilingualOrange)
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.47f))

        Image(
            painter = painterResource(R.drawable.img_logo),
            contentDescription = null,
//            modifier = Modifier.sharedElement() 이렇게 쓸거야 효빈씨
        )

        Spacer(Modifier.weight(0.53f))

        Image(
            painter = painterResource(R.drawable.img_login),
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

@Preview
@Composable
private fun AuthScreenPreview() {
    HilingualTheme {
        AuthScreen(
            paddingValues = PaddingValues(0.dp),
            onGoogleSignClick = {},
            onPrivacyPolicyClick = {}
        )
    }
}