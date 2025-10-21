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
package com.hilingual.core.ui.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.picker.HilingualBasicPicker
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.YearMonth

private val YEAR_RANGE = (2025..2100).map { "${it}년" }.toImmutableList()
private val MONTH_RANGE = (1..12).map { "${it}월" }.toImmutableList()

@Composable
fun HilingualYearMonthPicker(
    initialYearMonth: YearMonth,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    yearItems: ImmutableList<String> = YEAR_RANGE,
    monthItems: ImmutableList<String> = MONTH_RANGE,
    visibleItemsCount: Int = 3,
    itemSpacing: Dp = 10.dp,
    itemContentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 12.dp),
    spacingBetweenPickers: Dp = 44.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        val yearStartIndex = yearItems.indexOf("${initialYearMonth.year}년")
        val monthStartIndex = monthItems.indexOf("${initialYearMonth.monthValue}월")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                spacingBetweenPickers,
                Alignment.CenterHorizontally
            )
        ) {
            HilingualBasicPicker(
                items = yearItems,
                startIndex = yearStartIndex,
                onSelectedItemChanged = { selected ->
                    onYearSelected(selected.filter { it.isDigit() }.toInt())
                },
                modifier = Modifier.width(89.dp),
                itemContentPadding = itemContentPadding,
                itemSpacing = itemSpacing,
                visibleItemsCount = visibleItemsCount
            )

            HilingualBasicPicker(
                items = monthItems,
                startIndex = monthStartIndex,
                onSelectedItemChanged = { selected ->
                    onMonthSelected(selected.filter { it.isDigit() }.toInt())
                },
                modifier = Modifier.width(54.dp),
                itemContentPadding = itemContentPadding,
                itemSpacing = itemSpacing,
                visibleItemsCount = visibleItemsCount
            )
        }
    }
}
