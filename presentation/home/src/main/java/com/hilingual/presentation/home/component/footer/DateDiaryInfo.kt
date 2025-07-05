package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
internal fun DateDiaryInfo(
    selectedDateProvider: () -> LocalDate,
    isWrittenProvider: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val selectedDate = selectedDateProvider()
    val isWritten = isWrittenProvider()

    val formattedDate = remember(selectedDate) {
        selectedDate.format(DATE_FORMATTER)
    }
    val diaryStatusText = if (isWritten) "작성 완료" else "미작성"
    val diaryStatusColor =
        if (isWritten) HilingualTheme.colors.hilingualBlue else HilingualTheme.colors.gray300

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

private val DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)

private class IsWrittenPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview(showBackground = true)
@Composable
private fun DateDiaryInfoPreview(
    @PreviewParameter(IsWrittenPreviewParameterProvider::class) isWritten: Boolean
) {
    HilingualTheme {
        DateDiaryInfo(
            selectedDateProvider = { LocalDate.now() },
            isWrittenProvider = { isWritten }
        )
    }
}
