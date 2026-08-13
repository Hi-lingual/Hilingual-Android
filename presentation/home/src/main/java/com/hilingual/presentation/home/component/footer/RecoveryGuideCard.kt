package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun RecoveryGuideCard(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.gray100)
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "연속 작성이 끊겼어요.\n광고 한 편 보면 연속 기록을 살릴 수 있어요.",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray500,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun RecoveryGuideCardPreview() {
    HilingualTheme {
        RecoveryGuideCard()
    }
}
