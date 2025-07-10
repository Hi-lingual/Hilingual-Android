package com.hilingual.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SplashRoute(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            navigateToAuth()
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.hilingualOrange)
    ) {
        Spacer(
            modifier = Modifier.weight(18f)
        )

        Image(
            painter = painterResource(R.drawable.img_logo),
            contentDescription = null
        )

        Spacer(
            modifier = Modifier.weight(29f)
        )
    }
}
