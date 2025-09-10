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
package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun CalendarHeader(
    onDownArrowClick: () -> Unit,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
    yearMonth: () -> YearMonth,
    modifier: Modifier = Modifier
) {
    val currentYearMonth = yearMonth()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(HilingualTheme.colors.white)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.noRippleClickable(onClick = onDownArrowClick)
        ) {
            Text(
                text = "${currentYearMonth.year}년 ${
                    currentYearMonth.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                }",
                style = HilingualTheme.typography.headB18,
                color = HilingualTheme.colors.black
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down_24_black),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(28.dp)
                    .padding(2.dp)
            )
        }
        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .padding(2.dp)
                .noRippleClickable(onClick = onLeftArrowClick)
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_24),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .padding(2.dp)
                .noRippleClickable(onClick = onRightArrowClick)
        )
    }
}

@Preview
@Composable
private fun CalendarHeaderPreview() {
    HilingualTheme {
        CalendarHeader(
            onDownArrowClick = {},
            onLeftArrowClick = {},
            onRightArrowClick = {},
            modifier = Modifier,
            yearMonth = { YearMonth.now() }
        )
    }
}
