package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TitleLeftAlignedTopAppBar(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 16.dp, top = 15.dp, end = 16.dp, bottom = 14.dp),
        textAlign = TextAlign.Start,
        text = title,
        color = HilingualTheme.colors.white,
        style = HilingualTheme.typography.headM18
    )
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
