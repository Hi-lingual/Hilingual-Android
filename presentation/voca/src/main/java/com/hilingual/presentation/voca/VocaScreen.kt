package com.hilingual.presentation.voca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun VocaRoute(
    paddingValues: PaddingValues
) {
    VocaScreen(
        paddingValues = paddingValues
    )
}

@Composable
private fun VocaScreen(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "VOCA",
            style = HilingualTheme.typography.bodyB14
        )
    }
}