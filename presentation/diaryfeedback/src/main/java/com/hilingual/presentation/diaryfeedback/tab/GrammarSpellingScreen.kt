package com.hilingual.presentation.diaryfeedback.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun GrammarSpellingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100)
    ) {
        Text(
            text = "문법·철자 탭",
            style = HilingualTheme.typography.headM18
        )
    }
}
