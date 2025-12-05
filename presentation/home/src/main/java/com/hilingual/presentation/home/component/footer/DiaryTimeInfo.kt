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
package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlin.math.ceil

@Composable
internal fun DiaryTimeInfo(
    remainingTime: Int?,
    modifier: Modifier = Modifier
) {
    if (remainingTime != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_time_16),
                contentDescription = "남은 시간",
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(4.dp))

            val timeText = formatRemainingTime(remainingTime)

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = HilingualTheme.colors.hilingualOrange)) {
                    append(timeText)
                }
                withStyle(style = SpanStyle(color = HilingualTheme.colors.black)) {
                    append(" 남았어요")
                }
            }
            Text(
                text = annotatedString,
                style = HilingualTheme.typography.bodyM14
            )
        }
    }
}

private fun formatRemainingTime(remainingMinutes: Int): String = when {
    remainingMinutes > 60 -> "${ceil(remainingMinutes / 60.0).toInt()}시간"
    remainingMinutes >= 1 -> "${remainingMinutes}분"
    else -> "1분"
}

private data class DiaryTimeInfoPreviewState(
    val remainingTime: Int?
)

private class DiaryTimeInfoPreviewProvider :
    PreviewParameterProvider<DiaryTimeInfoPreviewState> {
    override val values = sequenceOf(
        DiaryTimeInfoPreviewState(remainingTime = 0), // 0m -> 1분
        DiaryTimeInfoPreviewState(remainingTime = 241), // 4h 01m -> 5시간
        DiaryTimeInfoPreviewState(remainingTime = 240), // 4h 00m -> 4시간
        DiaryTimeInfoPreviewState(remainingTime = 239), // 3h 59m -> 4시간
        DiaryTimeInfoPreviewState(remainingTime = 119), // 1h 59m -> 2시간
        DiaryTimeInfoPreviewState(remainingTime = 61), // 1h 1m -> 2시간
        DiaryTimeInfoPreviewState(remainingTime = 60), // 1h 0m -> 60분
        DiaryTimeInfoPreviewState(remainingTime = 59), // 59m -> 59분
        DiaryTimeInfoPreviewState(remainingTime = 1), // 1m -> 1분
        DiaryTimeInfoPreviewState(remainingTime = 0) // 0m -> 1분

    )
}

@Preview(showBackground = true)
@Composable
private fun DiaryTimeInfoPreview(
    @PreviewParameter(DiaryTimeInfoPreviewProvider::class) state: DiaryTimeInfoPreviewState
) {
    HilingualTheme {
        DiaryTimeInfo(
            remainingTime = state.remainingTime
        )
    }
}
