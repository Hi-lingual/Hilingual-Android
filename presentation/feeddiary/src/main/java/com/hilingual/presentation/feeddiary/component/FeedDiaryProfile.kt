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
package com.hilingual.presentation.feeddiary.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.util.formatRelativeTime
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.ProfileImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedDiaryProfile(
    profileUrl: String,
    nickname: String,
    streak: Int,
    isLiked: Boolean,
    likeCount: Int,
    sharedDateInMinutes: Long,
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(sharedDateInMinutes) { formatRelativeTime(sharedDateInMinutes) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        ProfileImage(
            imageUrl = profileUrl,
            modifier = Modifier
                .size(42.dp)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                )
                .noRippleClickable(onClick = onProfileClick)
        )

        Spacer(Modifier.width(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nickname,
                    style = HilingualTheme.typography.headB16,
                    color = HilingualTheme.colors.gray850
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire_16),
                    contentDescription = null,
                    tint = HilingualTheme.colors.hilingualOrange,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                )

                Text(
                    text = "$streak",
                    style = HilingualTheme.typography.captionR14,
                    color = HilingualTheme.colors.hilingualOrange,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Text(
                text = "$formattedDate 공유",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray500
            )
        }

        Spacer(Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = if (isLiked) R.drawable.ic_like_24_filled else R.drawable.ic_like_24_empty
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onLikeClick)
            )
            Text(
                text = "$likeCount",
                style = HilingualTheme.typography.captionM12,
                color = HilingualTheme.colors.gray850
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedDiaryProfilePreview() {
    HilingualTheme {
        FeedDiaryProfile(
            profileUrl = "",
            nickname = "작나",
            streak = 10,
            isLiked = true,
            likeCount = 6,
            sharedDateInMinutes = 6000,
            onProfileClick = {},
            onLikeClick = {}
        )
    }
}
