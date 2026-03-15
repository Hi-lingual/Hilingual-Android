package com.hilingual.core.ads.native

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun NativeLineAd(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AdMark()

        Text(
            text = title,
            color = HilingualTheme.colors.gray850,
            style = HilingualTheme.typography.bodyM12,
            maxLines = 1
        )

        Text(
            text = body,
            color = HilingualTheme.colors.gray850,
            style = HilingualTheme.typography.captionR12,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun NativeLineAdPreview() {
    HilingualTheme{
        NativeLineAd(
            title = "광고 이름",
            body = "메인 카피".repeat(20)
        )
    }
}
