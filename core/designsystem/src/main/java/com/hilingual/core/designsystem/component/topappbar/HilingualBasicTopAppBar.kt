package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = HilingualTheme.colors.white,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        ) {
            // 좌측 아이콘
            Row(
                modifier = Modifier.size(24.dp)
            ) {
                navigationIcon()
            }

            Spacer(modifier = Modifier.weight(1f))

            // title
            if (title != null) {
                Text(
                    text = title,
                    color = HilingualTheme.colors.black,
                    style = HilingualTheme.typography.headB18,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 우측 아이콘
            Row(
                modifier = Modifier.size(24.dp)
            ) {
                actions()
            }
        }
    }
}

@Preview
@Composable
private fun HilingualBasicTopAppBarPreview() {
    HilingualTheme {
        HilingualBasicTopAppBar()
    }
}