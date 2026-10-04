package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun NotificationReminderSaveDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "저장하지 않고 나가시겠어요?",
            description = "저장하지 않은 내용은 사라집니다.",
            cancelText = "취소하기",
            confirmText = "나가기",
            onNegative = onDismiss,
            onPositive = onConfirmClick,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationReminderSaveDialogPreview() {
    HilingualTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            NotificationReminderSaveDialog(
                isVisible = true,
                onDismiss = {},
                onConfirmClick = {},
            )
        }
    }
}
