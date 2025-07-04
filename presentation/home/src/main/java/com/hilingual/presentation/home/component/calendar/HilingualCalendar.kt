package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HilingualCalendar(
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid
    )

    HorizontalCalendar(
        state = state,
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .padding(horizontal = 16.dp),
        monthHeader = {
            DaysOfWeekTitle(
                daysOfWeek = daysOfWeek,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        dayContent = {
            Day(
                day = it,
                onClick = { day ->
                    selectedDate = day.date
                },
                isSelected = selectedDate == it.date
            )
        }
    )
}

@Composable
fun Day(
    day: CalendarDay,
    onClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isWritten: Boolean = false,
) {
    val textColor = when {
        isSelected -> HilingualTheme.colors.hilingualBlue
        isWritten -> HilingualTheme.colors.white
        day.position == DayPosition.MonthDate -> HilingualTheme.colors.hilingualBlack
        else -> HilingualTheme.colors.gray400
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .noRippleClickable { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        when {
            isSelected -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_filled_bubble_34),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            isWritten -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bubble_34),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            style = HilingualTheme.typography.bodySB12,
            color = textColor
        )
    }
}

@Composable
fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = HilingualTheme.typography.bodySB12,
                color = when (dayOfWeek) {
                    DayOfWeek.SATURDAY -> HilingualTheme.colors.hilingualBlue
                    DayOfWeek.SUNDAY -> HilingualTheme.colors.hilingualOrange
                    else -> HilingualTheme.colors.gray500
                }
            )
        }
    }
}

@Preview
@Composable
private fun HilingualCalendarPreview() {
    HilingualTheme {
        HilingualCalendar()
    }
}