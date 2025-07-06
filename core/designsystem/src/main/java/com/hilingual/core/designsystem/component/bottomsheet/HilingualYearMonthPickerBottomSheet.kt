package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.datapicker.HilingualYearMonthPicker
import com.hilingual.core.designsystem.component.datapicker.rememberPickerState
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualYearMonthPickerBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialYearMonth: YearMonth = YearMonth.now(),
    onDateSelected: (YearMonth) -> Unit,
) {
    val yearState = rememberPickerState(initialItem = initialYearMonth.year)
    val monthState = rememberPickerState(initialItem = initialYearMonth.monthValue)

    HilingualBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            HilingualYearMonthPicker(
                startLocalDate = initialYearMonth,
                yearPickerState = yearState,
                monthPickerState = monthState
            )
            Spacer(Modifier.height(19.dp))
            HilingualButton(
                text = "적용하기",
                onClick = {
                    onDateSelected(YearMonth.of(yearState.selectedItem, monthState.selectedItem))
                    onDismiss()
                }
            )
        }
    }
}

