package com.hilingual.presentation.diarywrite.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.theme.HilingualTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)

@Composable
internal fun DateText(
    selectedDateProvider: () -> LocalDate
) {
    val selectedDate = selectedDateProvider()

    val formattedDate = remember(selectedDate) {
        selectedDate.format(DATE_FORMATTER)
    }

    Text(
        text = formattedDate,
        style = HilingualTheme.typography.bodySB16,
        color = HilingualTheme.colors.black
    )
}

@Preview
@Composable
private fun DateTextPreview() {
    HilingualTheme {
        DateText(
            selectedDateProvider = { LocalDate.now() }
        )
    }
}