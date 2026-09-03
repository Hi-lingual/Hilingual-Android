package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.theme.HilingualTheme
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HilingualTimeInput(
    hour: Int,
    minute: Int,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    is24Hour: Boolean = false,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = is24Hour,
    )

    LaunchedEffect(hour, minute) {
        if (timePickerState.hour != hour) timePickerState.hour = hour
        if (timePickerState.minute != minute) timePickerState.minute = minute
    }

    LaunchedEffect(timePickerState) {
        snapshotFlow { timePickerState.hour to timePickerState.minute }
            .collect { (newHour, newMinute) ->
                if (newHour != hour || newMinute != minute) {
                    onTimeChange(newHour, newMinute)
                }
            }
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            onSurfaceVariant = HilingualTheme.colors.gray500
        ),
    ) {
        TimeInput(
            state = timePickerState,
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = TimePickerDefaults.colors(
                periodSelectorBorderColor = HilingualTheme.colors.gray200,
                periodSelectorSelectedContainerColor = HilingualTheme.colors.hilingualBlue50,
                periodSelectorUnselectedContainerColor = HilingualTheme.colors.white,
                periodSelectorSelectedContentColor = HilingualTheme.colors.hilingualBlue,
                periodSelectorUnselectedContentColor = HilingualTheme.colors.gray500,
                timeSelectorSelectedContainerColor = HilingualTheme.colors.white,
                timeSelectorUnselectedContainerColor = HilingualTheme.colors.gray100,
                timeSelectorSelectedContentColor = HilingualTheme.colors.black,
                timeSelectorUnselectedContentColor = HilingualTheme.colors.black,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HilingualTimeInputPreview() {
    var hour by remember { mutableStateOf(7) }
    var minute by remember { mutableStateOf(0) }

    HilingualTheme {
        HilingualTimeInput(
            hour = hour,
            minute = minute,
            onTimeChange = { newHour, newMinute ->
                hour = newHour
                minute = newMinute
            },
        )
    }
}
