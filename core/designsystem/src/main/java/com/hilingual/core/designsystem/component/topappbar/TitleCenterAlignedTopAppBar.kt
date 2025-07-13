package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TitleCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    title: String
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        title = title
    )
}

@Preview
@Composable
private fun TitleCenterAlignedTopAppBarPreview() {
    HilingualTheme {
        TitleCenterAlignedTopAppBar(
            title = "일기 작성하기"
        )
    }
}
