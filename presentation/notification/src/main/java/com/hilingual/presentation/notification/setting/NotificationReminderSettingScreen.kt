package com.hilingual.presentation.notification.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.toggle.HilingualBasicToggleSwitch
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.presentation.notification.setting.component.DayChip
import com.hilingual.presentation.notification.setting.component.DayOfWeek
import com.hilingual.presentation.notification.setting.component.HilingualTimeInput
import com.hilingual.presentation.notification.setting.component.NotificationReminderSaveDialog

@Composable
internal fun NotificationReminderSettingRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: NotificationReminderSettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isExitDialogVisible by remember { mutableStateOf(false) }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            NotificationReminderSettingSideEffect.NavigateUp -> navigateUp()
            NotificationReminderSettingSideEffect.ShowExitDialog -> isExitDialogVisible = true
        }
    }

    BackHandler(onBack = viewModel::onBackClick)

    when (val state = uiState) {
        is UiState.Success -> {
            NotificationReminderSettingScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onBackClick = viewModel::onBackClick,
                onTimeChange = viewModel::updateTime,
                onDailyRepeatChange = viewModel::updateDailyRepeat,
                onDayClick = viewModel::toggleDay,
                onSaveClick = viewModel::save,
            )

            NotificationReminderSaveDialog(
                isVisible = isExitDialogVisible,
                onDismiss = { isExitDialogVisible = false },
                onConfirmClick = {
                    isExitDialogVisible = false
                    viewModel.onExitConfirm()
                },
            )
        }
        is UiState.Loading -> {}
        is UiState.Failure -> {}
        else -> {}
    }
}

@Composable
private fun NotificationReminderSettingScreen(
    paddingValues: PaddingValues,
    uiState: NotificationReminderSettingUiState,
    onBackClick: () -> Unit,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    onDailyRepeatChange: (Boolean) -> Unit,
    onDayClick: (DayOfWeek) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
    ) {
        BackTopAppBar(title = "리마인드 시간 설정", onBackClicked = onBackClick)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "알림 시간",
            style = HilingualTheme.typography.bodyM15,
            color = HilingualTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        HilingualTimeInput(
            hour = uiState.hour,
            minute = uiState.minute,
            onTimeChange = onTimeChange,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "반복 요일",
                style = HilingualTheme.typography.bodyM15,
                color = HilingualTheme.colors.black,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "매일 반복",
                    style = HilingualTheme.typography.bodyR14,
                    color = HilingualTheme.colors.gray500,
                )
                Spacer(modifier = Modifier.width(4.dp))
                HilingualBasicToggleSwitch(
                    isChecked = uiState.isDailyRepeat,
                    onCheckedChange = onDailyRepeatChange,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DayOfWeek.entries.forEach { day ->
                DayChip(
                    label = day.label,
                    isSelected = day in uiState.selectedDays,
                    onClick = { if (!uiState.isDailyRepeat) onDayClick(day) },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        HilingualButton(
            text = "저장하기",
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationReminderSettingScreenPreview() {
    HilingualTheme {
        NotificationReminderSettingScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = NotificationReminderSettingUiState(
                hour = 9,
                minute = 30,
                isDailyRepeat = false,
                selectedDays = setOf(DayOfWeek.MON, DayOfWeek.WED, DayOfWeek.FRI),
            ),
            onBackClick = {},
            onTimeChange = { _, _ -> },
            onDailyRepeatChange = {},
            onDayClick = {},
            onSaveClick = {},
        )
    }
}
