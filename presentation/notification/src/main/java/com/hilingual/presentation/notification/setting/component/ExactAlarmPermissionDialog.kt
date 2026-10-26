package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun ExactAlarmPermissionDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "정시에 알림을 받으려면 권한이 필요해요",
            description = "알람 및 리마인더 권한을 허용하면\n설정한 시간에 정확히 알려드려요.",
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
private fun ExactAlarmPermissionDialogPreview() {
    HilingualTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ExactAlarmPermissionDialog(
                isVisible = true,
                onDismiss = {},
                onConfirmClick = {},
            )
        }
    }
}
