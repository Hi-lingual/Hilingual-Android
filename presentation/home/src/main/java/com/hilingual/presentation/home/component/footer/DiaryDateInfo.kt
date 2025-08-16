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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)

@Composable
internal fun DiaryDateInfo(
    selectedDate: LocalDate,
    isWritten: Boolean,
    isPublished: Boolean,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(selectedDate) {
        selectedDate.format(DATE_FORMATTER)
    }

    val isFutureDate = remember(selectedDate) {
        LocalDate.now().isBefore(selectedDate)
    }

    val (diaryStatusText, diaryStatusColor) = when {
        isFutureDate -> "작성 불가" to HilingualTheme.colors.gray300
        isWritten && isPublished -> "게시된 일기" to HilingualTheme.colors.hilingualBlue
        isWritten && !isPublished -> "비공개 일기" to HilingualTheme.colors.gray400
        else -> "미작성" to HilingualTheme.colors.gray300
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = formattedDate,
            style = HilingualTheme.typography.headB16,
            color = HilingualTheme.colors.black
        )

        Text(
            text = "·",
            style = HilingualTheme.typography.captionM12,
            color = HilingualTheme.colors.gray300
        )

        Text(
            text = diaryStatusText,
            style = HilingualTheme.typography.captionM12,
            color = diaryStatusColor
        )
    }
}

private data class DateDiaryInfoPreviewState(
    val date: LocalDate,
    val isWritten: Boolean,
    val isPublished: Boolean
)

private class DateDiaryInfoPreviewProvider : PreviewParameterProvider<DateDiaryInfoPreviewState> {
    override val values = sequenceOf(
        DateDiaryInfoPreviewState(LocalDate.now(), isWritten = true, isPublished = true),
        DateDiaryInfoPreviewState(LocalDate.now(), isWritten = true, isPublished = false),
        DateDiaryInfoPreviewState(LocalDate.now(), isWritten = false, isPublished = false),
        DateDiaryInfoPreviewState(LocalDate.now().plusDays(1), isWritten = false, isPublished = false),
        DateDiaryInfoPreviewState(LocalDate.now().minusDays(1), isWritten = false, isPublished = false)
    )
}

@Preview(showBackground = true)
@Composable
private fun DateDiaryInfoPreview(
    @PreviewParameter(DateDiaryInfoPreviewProvider::class) state: DateDiaryInfoPreviewState
) {
    HilingualTheme {
        DiaryDateInfo(
            selectedDate = state.date,
            isWritten = state.isWritten,
            isPublished = state.isPublished
        )
    }
}
