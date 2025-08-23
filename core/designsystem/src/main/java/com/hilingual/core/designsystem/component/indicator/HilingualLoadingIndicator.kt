package com.hilingual.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
