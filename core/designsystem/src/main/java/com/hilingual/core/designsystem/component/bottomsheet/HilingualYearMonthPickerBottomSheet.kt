package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.datapicker.HilingualYearMonthPicker
import com.hilingual.core.designsystem.theme.HilingualTheme
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualYearMonthPickerBottomSheet(
    onDismiss: () -> Unit,
    onDateSelected: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    initialYearMonth: YearMonth = YearMonth.now()
) {
    var selectedYear by remember { mutableIntStateOf(initialYearMonth.year) }
    var selectedMonth by remember { mutableIntStateOf(initialYearMonth.monthValue) }

    HilingualBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(19.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            HilingualYearMonthPicker(
                initialYearMonth = initialYearMonth,
                onYearSelected = { selectedYear = it },
                onMonthSelected = { selectedMonth = it }
            )

            HilingualButton(
                text = "적용하기",
                onClick = {
                    onDateSelected(YearMonth.of(selectedYear, selectedMonth))
                    onDismiss()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YearMonthPickerBottomSheetPreview() {
    HilingualTheme {
        var isDateBottomSheetVisible by remember { mutableStateOf(false) }
        var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }

        if (isDateBottomSheetVisible) {
            HilingualYearMonthPickerBottomSheet(
                onDismiss = { isDateBottomSheetVisible = false },
                onDateSelected = { yearMonth ->
                    selectedYearMonth = yearMonth
                },
                initialYearMonth = selectedYearMonth
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${selectedYearMonth.year}년 ${selectedYearMonth.monthValue}월",
                style = HilingualTheme.typography.bodyB14,
                modifier = Modifier
                    .padding(5.dp)
                    .noRippleClickable(
                        onClick = { isDateBottomSheetVisible = true }
                    )
            )
        }
    }
}
