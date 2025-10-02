package com.hilingual.presentation.mypage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun LogoutDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onLogoutClick: () -> Unit
) {
    if (isVisible) {
        TwoButtonDialog(
            title = "하이링구얼에서 로그아웃 하시겠어요?",
            cancelText = "아니요",
            confirmText = "로그아웃",
            onNegative = onDismiss,
            onPositive = onLogoutClick,
            onDismiss = onDismiss
        )
    }
}

@Preview
@Composable
private fun LogoutDialogPreview() {
    HilingualTheme {
        LogoutDialog(
            isVisible = true,
            onDismiss = {},
            onLogoutClick = {}
        )
    }
}
