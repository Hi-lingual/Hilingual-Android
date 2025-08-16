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
package com.hilingual.presentation.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun HomeHeader(
    imageUrl: String,
    nickname: String,
    totalDiaries: Int,
    streak: Int,
    isNewAlarm: Boolean,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileImage(imageUrl)

        Column(modifier = Modifier.weight(1f)) {
            ProfileName(nickname)
            UserStatsRow(totalDiaries, streak)
        }

        Box {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_alarm_28),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable(onClick = onAlarmClick)
            )
            if (isNewAlarm) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_dot_4),
                    contentDescription = null,
                    tint = HilingualTheme.colors.alertRed,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun ProfileImage(imageUrl: String) {
    if (imageUrl.isBlank()) {
        Image(
            painter = painterResource(R.drawable.img_default_image),
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
        )
    } else {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.size(46.dp)
        )
    }
}

@Composable
private fun ProfileName(name: String) {
    Text(
        text = name,
        style = HilingualTheme.typography.headB18,
        color = HilingualTheme.colors.white,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun UserStatsRow(totalDiaries: Int, streak: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DiaryCountStat(totalDiaries)

        StatSeparator()

        StreakStat(streak)
    }
}

@Composable
private fun DiaryCountStat(count: Int) {
    StatItem(
        icon = R.drawable.ic_bubble_16,
        text = if (count > 0) "총 ${count}편" else "일기를 작성해보세요!"
    )
}

@Composable
private fun StreakStat(days: Int) {
    StatItem(
        icon = R.drawable.ic_fire_16,
        text = "${days}일 연속 작성 중"
    )
}

@Composable
private fun StatItem(@DrawableRes icon: Int, text: String) {
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = null,
        tint = Color.Unspecified
    )

    Text(
        text = text,
        style = HilingualTheme.typography.captionM12,
        color = HilingualTheme.colors.white,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun StatSeparator() {
    Text(
        text = "·",
        style = HilingualTheme.typography.captionM12,
        color = HilingualTheme.colors.gray100
    )
}

@Preview
@Composable
private fun HomeHeaderPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.background(HilingualTheme.colors.black)
        ) {
            HomeHeader(
                imageUrl = "",
                nickname = "닉네임1",
                totalDiaries = 10,
                streak = 5,
                isNewAlarm = true,
                onAlarmClick = {}
            )
            HomeHeader(
                imageUrl = "",
                nickname = "닉네임2",
                totalDiaries = 0,
                streak = 0,
                isNewAlarm = false,
                onAlarmClick = {}
            )
        }
    }
}
