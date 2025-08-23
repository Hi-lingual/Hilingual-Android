package com.hilingual.presentation.feeddiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedDiaryRoute(
    paddingValues: PaddingValues
) {
    FeedDiaryScreen(
        paddingValues = paddingValues
    )
}

@Composable
private fun FeedDiaryScreen(
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {

    }
}