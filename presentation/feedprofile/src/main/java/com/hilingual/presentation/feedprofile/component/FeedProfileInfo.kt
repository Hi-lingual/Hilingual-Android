package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedProfileInfo(
    profileUrl: String,
    nickname: String,
    follower: Int,
    following: Int,
    onFollowTypeClick: () -> Unit,
    streak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        NetworkImage(
            imageUrl = profileUrl,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .size(60.dp)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                )
        )
        Column {
            Text(
                text = nickname,
                style = HilingualTheme.typography.headB18,
                color = HilingualTheme.colors.gray850,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .noRippleClickable(onClick = onFollowTypeClick)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "팔로워",
                    style = HilingualTheme.typography.captionR14,
                    color = HilingualTheme.colors.gray400,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "$follower",
                    style = HilingualTheme.typography.bodyB14,
                    color = HilingualTheme.colors.gray400,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "팔로잉",
                    style = HilingualTheme.typography.captionR14,
                    color = HilingualTheme.colors.gray400,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "$following",
                    style = HilingualTheme.typography.bodyB14,
                    color = HilingualTheme.colors.gray400
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire_16),
                    contentDescription = null,
                    tint = HilingualTheme.colors.hilingualOrange,
                    modifier = Modifier
                        .padding(end = 1.dp)
                        .size(16.dp)
                )
                Text(
                    text = "${streak}일 연속 작성중",
                    style = HilingualTheme.typography.bodyM14,
                    color = if (streak > 0) HilingualTheme.colors.hilingualOrange else HilingualTheme.colors.gray400
                )
            }
        }
    }
}

@Preview()
@Composable
private fun FeedProfileInfoPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeedProfileInfo(
                profileUrl = "",
                nickname = "하이링",
                follower = 123,
                following = 123,
                onFollowTypeClick = {},
                streak = 7
            )
            FeedProfileInfo(
                profileUrl = "",
                nickname = "하이링",
                follower = 123,
                following = 123,
                onFollowTypeClick = {},
                streak = 0
            )
        }
    }
}
