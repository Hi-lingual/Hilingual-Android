package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.collections.immutable.persistentListOf

/* 버튼 타입 구분 */
enum class ButtonType() {
    FILLED, OUTLINED
}

@Composable
fun BasicFollowButton(
    type: ButtonType,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = HilingualTheme.colors
    val buttonColor = remember(type) {
        when (type) {
            ButtonType.FILLED -> {
                UserButtonColor(
                    border = colors.hilingualBlack,
                    background = colors.hilingualBlack,
                    text = colors.white
                )
            }

            ButtonType.OUTLINED -> {
                UserButtonColor(
                    border = colors.gray200,
                    background = colors.white,
                    text = colors.gray500
                )
            }
        }
    }

    Text(
        text = buttonText,
        color = buttonColor.text,
        style = HilingualTheme.typography.bodySB14,
        textAlign = TextAlign.Center,
        modifier = modifier
            .noRippleClickable(
                onClick = onClick
            )
            .width(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor.background)
            .border(
                width = 1.dp,
                color = buttonColor.border,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 8.dp)
    )
}

data class UserButtonColor(
    val border: Color,
    val background: Color,
    val text: Color
)

enum class ApiType() {
    CANCEL, // 팔로우 해제
    FOLLOW // 팔로우 등록
}

enum class FollowState(
    var text: String,
    var type: ButtonType,
    var followInfo: Int,
    var apiType: ApiType // 버튼 클릭 시 호출할 API 유형
) {
    //TODO: 차단 처리 고민
    FOLLOWING(
        text = "팔로잉",
        type = ButtonType.OUTLINED,
        followInfo = 1,
        apiType = ApiType.CANCEL
    ),
    FOLLOW(
        text = "팔로우",
        type = ButtonType.FILLED,
        followInfo = 2,
        apiType = ApiType.CANCEL
    ),
    MUTUAL_FOLLOW(
        text = "맞팔로우",
        type = ButtonType.FILLED,
        followInfo = 3,
        apiType = ApiType.FOLLOW
    );

    companion object {
        fun findFollowState(followStateCode: Int) = entries.find {
            it.followInfo == followStateCode
        } ?: FOLLOWING
    }
}

@Composable
fun FollowItem(
    profileUrl: String,
    nickname: String,
    followState: FollowState,
    onClickProfile: () -> Unit,
    onClickButton: () -> Unit,
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
                onClick = onClickProfile //TODO: 프로필 화면으로 이동 (userId 필요)
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
                    ),
            )

            Text(
                text = nickname,
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.black
            )
        }

        BasicFollowButton(
            type = followState.type,
            buttonText = followState.text,
            onClick = onClickButton //TODO: API 호출 (userId 필요)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun UserItemPreview() {
    HilingualTheme {
        val followState = FollowState.findFollowState(1)
        val buttonClickEvent = when (followState.apiType) {
            ApiType.FOLLOW -> { {} } // 팔로우 등록 API 호출
            ApiType.CANCEL -> { {} } // 팔로우 해제 API 호출
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FollowItem(
                profileUrl = "",
                nickname = "Android1",
                followState = followState,
                onClickProfile = {},
                onClickButton = buttonClickEvent,
                modifier = Modifier.fillMaxWidth()
            )
            FollowItem(
                profileUrl = "",
                nickname = "Android2",
                followState = FollowState.FOLLOW,
                onClickProfile = {},
                onClickButton = {},
                modifier = Modifier.fillMaxWidth()
            )
            FollowItem(
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

@Preview(showBackground = true)
@Composable
private fun BasicButtonPreview() {
    HilingualTheme {
        val followStateList = persistentListOf<FollowState>(
            FollowState.FOLLOWING,
            FollowState.findFollowState(followStateCode = 2), // follow
            FollowState.MUTUAL_FOLLOW
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(followStateList) {
                BasicFollowButton(
                    type = it.type,
                    buttonText = it.text,
                    onClick = {}
                )
            }
        }
    }
}