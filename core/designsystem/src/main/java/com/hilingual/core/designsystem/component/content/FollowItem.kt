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
import com.hilingual.core.designsystem.component.button.ApiType
import com.hilingual.core.designsystem.component.button.FollowButton
import com.hilingual.core.designsystem.component.button.FollowState
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun FollowItem(
    userId: Long,
    profileUrl: String,
    nickname: String,
    followState: FollowState,
    onClickProfile: (Long) -> Unit,
    onClickButton: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.noRippleClickable(
                onClick = { onClickProfile(userId) }
            )
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

        FollowButton(
            type = followState.type,
            buttonText = followState.text,
            onClick = { onClickButton(userId) }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun FollowItemPreview() {
    HilingualTheme {
        val followState = FollowState.findFollowState(1)
        val buttonClickEvent: (Long) -> Unit = { userId -> // userId를 파라미터로 받도록 변경
            when (followState.apiType) {
                ApiType.FOLLOW -> {}
                ApiType.CANCEL -> {}
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FollowItem(
                userId = 0L,
                profileUrl = "",
                nickname = "Android1",
                followState = followState,
                onClickProfile = {},
                onClickButton = buttonClickEvent,
                modifier = Modifier.fillMaxWidth()
            )
            FollowItem(
                userId = 0L,
                profileUrl = "",
                nickname = "Android2",
                followState = FollowState.FOLLOW,
                onClickProfile = {},
                onClickButton = {},
                modifier = Modifier.fillMaxWidth()
            )
            FollowItem(
                userId = 0L,
                profileUrl = "",
                nickname = "Android3",
                followState = FollowState.MUTUAL_FOLLOW,
                onClickProfile = {},
                onClickButton = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
