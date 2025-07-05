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
    selectedDateProvider: () -> LocalDate,
    isWrittenProvider: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val selectedDate = selectedDateProvider()
    val isWritten = isWrittenProvider()

    val formattedDate = remember(selectedDate) {
        selectedDate.format(DATE_FORMATTER)
    }

    val isFutureDate = remember(selectedDate) {
        LocalDate.now().isBefore(selectedDate)
    }

    val diaryStatusText = when {
        isFutureDate -> "작성 불가"
        isWritten -> "작성 완료"
        else -> "미작성"
    }
    val diaryStatusColor = when {
        isFutureDate -> HilingualTheme.colors.gray300
        isWritten -> HilingualTheme.colors.hilingualBlue
        else -> HilingualTheme.colors.gray300
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
    val isWritten: Boolean
)

private class DateDiaryInfoPreviewProvider : PreviewParameterProvider<DateDiaryInfoPreviewState> {
    override val values = sequenceOf(
        DateDiaryInfoPreviewState(LocalDate.now(), true), // 작성 완료
        DateDiaryInfoPreviewState(LocalDate.now(), false), // 미작성
        DateDiaryInfoPreviewState(LocalDate.now().plusDays(1), false) // 작성 불가 (미래)
    )
}

@Preview(showBackground = true)
@Composable
private fun DateDiaryInfoPreview(
    @PreviewParameter(DateDiaryInfoPreviewProvider::class) state: DateDiaryInfoPreviewState
) {
    HilingualTheme {
        DiaryDateInfo(
            selectedDateProvider = { state.date },
            isWrittenProvider = { state.isWritten }
        )
    }
}
