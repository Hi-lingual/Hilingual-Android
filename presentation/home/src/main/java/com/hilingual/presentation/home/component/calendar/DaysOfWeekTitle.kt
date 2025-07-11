package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun DaysOfWeekTitle(
    daysOfWeek: ImmutableList<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = HilingualTheme.typography.bodySB12,
                color = when (dayOfWeek) {
                    DayOfWeek.SATURDAY -> HilingualTheme.colors.hilingualBlue
                    DayOfWeek.SUNDAY -> HilingualTheme.colors.alertRed
                    else -> HilingualTheme.colors.gray500
                }
            )
        }
    }
}
