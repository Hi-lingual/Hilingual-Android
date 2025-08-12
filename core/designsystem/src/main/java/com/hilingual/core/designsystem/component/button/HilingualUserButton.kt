package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
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
            .noRippleClickable(onClick = onClick)
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

@Composable
fun UserItem(
    profileImageUrl: String,
    nickname: String,
    buttonStateValue: Int,
    onProfileClick: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(vertical = 8.dp)
            .noRippleClickable(onClick = onProfileClick),
        verticalAlignment = Alignment.CenterVertically
    ) {

        NetworkImage(
            imageUrl = profileImageUrl,
            shape = CircleShape,
            modifier = Modifier
                .size(42.dp)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = nickname,
            style = HilingualTheme.typography.headB16,
            color = HilingualTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        FollowButton(
            buttonStateValue = buttonStateValue,
            onClick = onButtonClick
        )
    }
}

@Preview()
@Composable
fun UserItemPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserItem(
                profileImageUrl = "",
                nickname = "닉네임",
                onProfileClick = { },
                onButtonClick = { },
                buttonStateValue = 1,
            )
            UserItem(
                profileImageUrl = "",
                nickname = "닉네임",
                onProfileClick = { },
                onButtonClick = { },
                buttonStateValue = 2,
            )
            UserItem(
                profileImageUrl = "",
                nickname = "닉네임",
                onProfileClick = { },
                onButtonClick = { },
                buttonStateValue = 3,
            )
        }
    }
}
