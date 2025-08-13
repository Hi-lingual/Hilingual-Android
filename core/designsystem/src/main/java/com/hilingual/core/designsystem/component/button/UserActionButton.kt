package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun UserActionButton(
    isPressed: Boolean,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (buttonColor, textColor, border) = if (isPressed) {
        Triple(
            HilingualTheme.colors.white,
            HilingualTheme.colors.gray500,
            BorderStroke(width = 1.dp, color = HilingualTheme.colors.gray200)
        )
    } else {
        Triple(
            HilingualTheme.colors.hilingualBlack,
            HilingualTheme.colors.white,
            BorderStroke(width = 1.dp, color = HilingualTheme.colors.hilingualBlack)
        )
    }

    Text(
        text = buttonText,
        color = textColor,
        style = HilingualTheme.typography.bodySB14,
        textAlign = TextAlign.Center,
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .width(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor)
            .border(
                border = border,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun UserActionButtonPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserActionButton(
                isPressed = false,
                buttonText = "팔로우",
                onClick = {}
            )
            UserActionButton(
                isPressed = false,
                buttonText = "맞팔로우",
                onClick = {}
            )
            UserActionButton(
                isPressed = true,
                buttonText = "팔로잉",
                onClick = {}
            )
        }
    }
}
