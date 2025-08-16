package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun FeedUserActionButton(
    isFilled: Boolean,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (buttonColor, textColor, borderColor) = if (isFilled) {
        Triple(
            HilingualTheme.colors.white,
            HilingualTheme.colors.gray500,
            HilingualTheme.colors.gray200
        )
    } else {
        Triple(
            HilingualTheme.colors.hilingualBlack,
            HilingualTheme.colors.white,
            HilingualTheme.colors.hilingualBlack
        )
    }

    Text(
        text = buttonText,
        color = textColor,
        style = HilingualTheme.typography.bodySB14,
        textAlign = TextAlign.Center,
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 10.dp)
    )
}


@Preview(showBackground = true)
@Composable
private fun FeedUserActionButtonPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            FeedUserActionButton(
                isFilled = false,
                buttonText = "팔로우",
                onClick = {}
            )
            FeedUserActionButton(
                isFilled = false,
                buttonText = "맞팔로우",
                onClick = {}
            )
            FeedUserActionButton(
                isFilled = false,
                buttonText = "차단",
                onClick = {}
            )
            FeedUserActionButton(
                isFilled = true,
                buttonText = "팔로잉",
                onClick = {}
            )
            FeedUserActionButton(
                isFilled = true,
                buttonText = "차단 해제하기",
                onClick = {}
            )
        }
    }
}
