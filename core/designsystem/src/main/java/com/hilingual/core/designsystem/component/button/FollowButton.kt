package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.persistentListOf

enum class ButtonType() {
    FILLED, OUTLINED
}

@Composable
fun FollowButton(
    type: ButtonType,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = HilingualTheme.colors
    val buttonColor = remember(type) {
        when (type) {
            ButtonType.FILLED -> {
                ButtonColor(
                    border = colors.hilingualBlack,
                    background = colors.hilingualBlack,
                    text = colors.white
                )
            }

            ButtonType.OUTLINED -> {
                ButtonColor(
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

private data class ButtonColor(
    val border: Color,
    val background: Color,
    val text: Color
)

enum class ApiType() {
    FOLLOW, CANCEL
}

enum class FollowState(
    var text: String,
    var type: ButtonType,
    var followInfo: Int,
    var apiType: ApiType
) {
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

@Preview(showBackground = true)
@Composable
private fun FollowButtonPreview() {
    HilingualTheme {
        val followStateList = persistentListOf<FollowState>(
            FollowState.FOLLOWING,
            FollowState.findFollowState(followStateCode = 2), // follow
            FollowState.MUTUAL_FOLLOW
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(followStateList) {
                FollowButton(
                    type = it.type,
                    buttonText = it.text,
                    onClick = {}
                )
            }
        }
    }
}
