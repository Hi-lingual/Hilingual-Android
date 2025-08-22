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
package com.hilingual.presentation.notification.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

private val BOLD_NAME_REGEX = Regex("(\\S+님)")

@Composable
internal fun NotificationItem(
    title: String,
    date: String,
    isRead: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typo = HilingualTheme.typography
    val colors = HilingualTheme.colors
    val backgroundColor = if (isRead) colors.white else colors.gray100

    Row(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .fillMaxWidth()
            .border(1.dp, colors.gray100)
            .background(backgroundColor)
            .padding(vertical = 20.dp)
            .padding(start = 16.dp, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(title)
                    BOLD_NAME_REGEX.find(title)?.let { matchResult ->
                        addStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold),
                            start = matchResult.range.first,
                            end = matchResult.range.last + 1
                        )
                    }
                },
                style = typo.bodyM16,
                color = colors.black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = date,
                style = typo.captionR14,
                color = colors.gray300
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_24),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun NotificationItemPreview() {
    HilingualTheme {
        Column {
            NotificationItem(
                title = "방금 이병건님이 당신의 8월 14일 일기에 공감했습니다.",
                date = "2025-08-15",
                isRead = false,
                onClick = {}
            )
            NotificationItem(
                title = "토착왜구맨님이 당신의 8월 14일 일기에 공감했습니다.",
                date = "2025-08-15",
                isRead = true,
                onClick = {}
            )
            NotificationItem(
                title = "v.1.1.0 업데이트 알림",
                date = "2025-08-15",
                isRead = false,
                onClick = {}
            )
            NotificationItem(
                title = "시스템 점검에 따른 사용 중단 안내",
                date = "2025-08-15",
                isRead = true,
                onClick = {}
            )
        }
    }
}
