package com.hilingual.core.ads.native

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun AdMark(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(HilingualTheme.colors.gray400)
            .padding(vertical = 1.dp, horizontal = 3.dp)
            .widthIn(min = 24.dp)
            .heightIn(min = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AD",
            color = HilingualTheme.colors.white,
            style = HilingualTheme.typography.bodyM12,
        )
    }
}

@Preview
@Composable
private fun AdMarkPreview() {
    HilingualTheme {
        AdMark()
    }
}
