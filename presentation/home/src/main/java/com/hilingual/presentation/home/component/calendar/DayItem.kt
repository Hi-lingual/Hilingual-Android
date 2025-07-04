package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
internal fun DayItem(
    day: CalendarDay,
    onClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isWritten: Boolean = false,
) {
    val textColor = when {
        isSelected -> HilingualTheme.colors.hilingualBlue
        isWritten -> HilingualTheme.colors.white
        day.position == DayPosition.MonthDate -> HilingualTheme.colors.black
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

        if (day.date == LocalDate.now())
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_today_4),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
    }
}