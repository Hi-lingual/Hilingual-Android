package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.gray200
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white


private enum class ButtonState(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color,
    val horizontalPadding: Float,
    val border: BorderStroke? = null,
) {
    FOLLOWING(
        text = "팔로잉",
        textColor = gray500,
        backgroundColor = white,
        horizontalPadding = 21.5f,
        border = BorderStroke(1.dp, gray200)
    ),
    FOLLOW(
        text = "팔로우",
        textColor = white,
        backgroundColor = hilingualBlack,
        horizontalPadding = 21.5f,
        border = null
    ),
    FOLLOWMEBACK(
        text = "맞팔로우",
        textColor = white,
        backgroundColor = hilingualBlack,
        horizontalPadding = 15.5f,
        border = null
    )
}

@Composable
fun FollowButton(
    buttonStateValue: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonState = remember(buttonStateValue) {
        when (buttonStateValue) {
            1 -> ButtonState.FOLLOWING
            2 -> ButtonState.FOLLOW
            3 -> ButtonState.FOLLOWMEBACK
            else -> ButtonState.FOLLOW
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = buttonState.backgroundColor)
            .then(
                if (buttonState.border != null) {
                    Modifier.border(
                        border = buttonState.border,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = buttonState.horizontalPadding.dp, vertical = 8.dp)
    ) {
        Text(
            text = buttonState.text,
            color = buttonState.textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FollowButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FollowButton(
            buttonStateValue = 1,
            onClick = { }
        )

        FollowButton(
            buttonStateValue = 2,
            onClick = { }
        )

        FollowButton(
            buttonStateValue = 3,
            onClick = { }
        )

    }
}