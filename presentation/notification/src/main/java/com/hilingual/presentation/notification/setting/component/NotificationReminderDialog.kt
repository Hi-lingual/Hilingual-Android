package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun NotificationReminderDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "기기의 알림 설정이 꺼져있어요!",
            description = "리마인드 알림을 받기 위해선\n기기 알림 설정이 켜져 있어야 해요.",
            cancelText = "취소하기",
            confirmText = "설정 변경하기",
            onNegative = onDismiss,
            onPositive = onConfirmClick,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationReminderDialogPreview() {
    HilingualTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            NotificationReminderDialog(
                isVisible = true,
                onDismiss = {},
                onConfirmClick = {},
            )
        }
    }
}
