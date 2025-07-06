package com.hilingual.core.designsystem.component.datapicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.YearMonth

private val CURRENT_YEAR_MONTH = YearMonth.now()
private val YEAR_RANGE = (1000..9999).map { "${it}년" }.toImmutableList()
private val MONTH_RANGE = (1..12).map { "${it}월" }.toImmutableList()

@Composable
fun HilingualYearMonthPicker(
    modifier: Modifier = Modifier,
    yearPickerState: PickerState<Int> = rememberPickerState(CURRENT_YEAR_MONTH.year),
    monthPickerState: PickerState<Int> = rememberPickerState(CURRENT_YEAR_MONTH.monthValue),
    startLocalDate: YearMonth = CURRENT_YEAR_MONTH,
    yearItems: ImmutableList<String> = YEAR_RANGE,
    monthItems: ImmutableList<String> = MONTH_RANGE,
    visibleItemsCount: Int = 3,
    itemPadding: PaddingValues = PaddingValues(10.dp),
    spacingBetweenPickers: Dp = 44.dp,
) {
    Surface(modifier = modifier.background(HilingualTheme.colors.white)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            val yearStartIndex = remember {
                val matchYear = yearItems.find {
                    it.contains(startLocalDate.year.toString())
                }
                yearItems.indexOf(matchYear)
            }
            val monthStartIndex = remember {
                val matchMonth = yearItems.find {
                    it.contains(startLocalDate.monthValue.toString())
                }
                monthItems.indexOf(matchMonth)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    spacingBetweenPickers,
                    Alignment.CenterHorizontally
                ),
            ) {
                HilingualBasicPicker(
                    state = yearPickerState,
                    modifier = Modifier.width(89.dp),
                    items = yearItems,
                    startIndex = yearStartIndex,
                    itemPadding = itemPadding,
                )
                HilingualBasicPicker(
                    state = monthPickerState,
                    items = monthItems,
                    startIndex = monthStartIndex,
                    visibleItemsCount = visibleItemsCount,
                    modifier = Modifier.width(54.dp),
                    itemPadding = itemPadding,
                )
            }
        }
    }
}