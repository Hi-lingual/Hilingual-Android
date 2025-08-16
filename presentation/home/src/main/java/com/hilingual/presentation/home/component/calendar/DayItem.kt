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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
internal fun DayItem(
    day: CalendarDay,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isWritten: Boolean = false
) {
    val textColor = when {
        isSelected -> HilingualTheme.colors.white
        isWritten -> HilingualTheme.colors.hilingualBlue
        day.position == DayPosition.MonthDate -> HilingualTheme.colors.black
        else -> HilingualTheme.colors.gray200
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when {
            isSelected -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bubble_34),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            isWritten -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bubble_34),
                    contentDescription = null,
                    tint = HilingualTheme.colors.hilingualBlue50
                )
            }
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            style = HilingualTheme.typography.bodySB14,
            color = textColor
        )

        if (day.date == LocalDate.now()) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_today_4),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
