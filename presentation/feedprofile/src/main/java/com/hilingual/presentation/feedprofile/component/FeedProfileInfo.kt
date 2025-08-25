/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    isMine: Boolean,
    isFollowing: Boolean,
    isFollowed: Boolean,
    isBlock: Boolean,
    onActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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

        if (!isMine) {
            FeedUserActionButton(
                isFilled = isFollowing,
                buttonText = when {
                    isBlock -> "차단 해제"
                    isFollowing -> "팔로잉"
                    isFollowed -> "맞팔로우"
                    else -> "팔로우"
                },
                onClick = onActionButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 8.dp)

            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun FeedProfileInfoPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FeedProfileInfo(
                profileUrl = "",
                nickname = "하이링",
                follower = 123,
                following = 456,
                streak = 7,
                isMine = false,
                isFollowing = true,
                isFollowed = true,
                isBlock = false,
                onFollowTypeClick = {},
                onActionButtonClick = {}
            )
            FeedProfileInfo(
                profileUrl = "",
                nickname = "내 계정",
                follower = 99,
                following = 77,
                streak = 0,
                isMine = true,
                isFollowing = false,
                isFollowed = false,
                isBlock = false,
                onFollowTypeClick = {},
                onActionButtonClick = {}
            )
        }
    }
}
