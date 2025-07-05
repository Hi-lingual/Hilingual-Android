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
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun CalendarHeader(
    onDownArrowClick: () -> Unit,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
    yearMonth: () -> YearMonth,
    modifier: Modifier = Modifier,
) {
    val currentYearMonth = yearMonth()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(HilingualTheme.colors.white)
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
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down_24),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onDownArrowClick)
                .align(Alignment.Bottom)
        )
        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_28),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = onLeftArrowClick)
        )
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_28),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = onRightArrowClick)
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
