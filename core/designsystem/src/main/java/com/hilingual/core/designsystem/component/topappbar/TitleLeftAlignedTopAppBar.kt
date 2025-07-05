package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TitleLeftAlignedTopAppBar(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = HilingualTheme.colors.white,
            style = HilingualTheme.typography.headM18
        )
    }
}

@Preview
@Composable
private fun TitleLeftAlignedTopAppBarPreview() {
    HilingualTheme {
        TitleLeftAlignedTopAppBar(
            title = "나의 단어장"
        )
    }
}