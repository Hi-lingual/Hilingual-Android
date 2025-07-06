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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val YEAR_RANGE = (1000..9999).map { "${it}년" }.toImmutableList()
private val MONTH_RANGE = (1..12).map { "${it}월" }.toImmutableList()

@Composable
fun HilingualYearMonthPicker(
    selectedYear: Int,
    selectedMonth: Int,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
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
            val yearStartIndex = yearItems.indexOf("${selectedYear}년")
            val monthStartIndex = monthItems.indexOf("${selectedMonth}월")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    spacingBetweenPickers,
                    Alignment.CenterHorizontally
                ),
            ) {
                HilingualBasicPicker(
                    items = yearItems,
                    startIndex = yearStartIndex,
                    onSelectedItemChanged = { selected ->
                        onYearSelected(selected.filter { it.isDigit() }.toInt())
                    },
                    modifier = Modifier.width(89.dp),
                    itemPadding = itemPadding,
                )

                HilingualBasicPicker(
                    items = monthItems,
                    startIndex = monthStartIndex,
                    onSelectedItemChanged = { selected ->
                        onMonthSelected(selected.filter { it.isDigit() }.toInt())
                    },
                    modifier = Modifier.width(54.dp),
                    itemPadding = itemPadding,
                    visibleItemsCount = visibleItemsCount
                )
            }
        }
    }
}
