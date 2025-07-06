package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.datapicker.HilingualYearMonthPicker
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualYearMonthPickerBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialYearMonth: YearMonth = YearMonth.now(),
    onDateSelected: (YearMonth) -> Unit,
) {
    var selectedYear by remember { mutableIntStateOf(initialYearMonth.year) }
    var selectedMonth by remember { mutableIntStateOf(initialYearMonth.monthValue) }

    HilingualBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            HilingualYearMonthPicker(
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                onYearSelected = { selectedYear = it },
                onMonthSelected = { selectedMonth = it },
            )

            Spacer(Modifier.height(19.dp))

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

