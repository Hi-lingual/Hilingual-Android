package com.hilingual.presentation.voca.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun ReviewAnswerButtons(
    onUnknownClick: () -> Unit,
    onKnownClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ReviewAnswerButton(
            text = "몰라요",
            backgroundColor = HilingualTheme.colors.white,
            textColor = HilingualTheme.colors.black,
            onClick = onUnknownClick,
            modifier = Modifier.weight(1f),
        )
        ReviewAnswerButton(
            text = "알아요",
            backgroundColor = HilingualTheme.colors.hilingualBlack,
            textColor = HilingualTheme.colors.white,
            onClick = onKnownClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ReviewAnswerButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = HilingualTheme.typography.bodyM16,
            color = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewAnswerButtonsPreview() {
    HilingualTheme {
        ReviewAnswerButtons(
            onUnknownClick = {},
            onKnownClick = {},
        )
    }
}
