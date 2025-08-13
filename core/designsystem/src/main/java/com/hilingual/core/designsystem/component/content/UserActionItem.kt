package com.hilingual.core.designsystem.component.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.button.UserActionButton
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun UserActionItem(
    userId: Long,
    profileUrl: String,
    nickname: String,
    isPressed: Boolean,
    buttonText: String,
    onProfileClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.noRippleClickable(onClick = { onProfileClick(userId) })
        ) {
            NetworkImage(
                imageUrl = profileUrl,
                shape = CircleShape,
                modifier = Modifier
                    .size(42.dp)
                    .border(
                        width = 1.dp,
                        color = HilingualTheme.colors.gray200,
                        shape = CircleShape
                    )
            )

            Text(
                text = nickname,
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.black
            )
        }

        UserActionButton(
            isPressed = isPressed,
            buttonText = buttonText,
            onClick = { onButtonClick(userId) }
        )
    }
}

@Preview()
@Composable
private fun UserActionItemPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserActionItem(
                userId = 1L,
                profileUrl = "",
                nickname = "나현",
                isPressed = false,
                buttonText = "팔로우",
                onProfileClick = {},
                onButtonClick = {}
            )
            UserActionItem(
                userId = 2L,
                profileUrl = "",
                nickname = "작나",
                isPressed = false,
                buttonText = "맞팔로우",
                onProfileClick = {},
                onButtonClick = {}
            )
            UserActionItem(
                userId = 3L,
                profileUrl = "",
                nickname = "큰나",
                isPressed = true,
                buttonText = "팔로잉",
                onProfileClick = {},
                onButtonClick = {}
            )
        }
    }
}
