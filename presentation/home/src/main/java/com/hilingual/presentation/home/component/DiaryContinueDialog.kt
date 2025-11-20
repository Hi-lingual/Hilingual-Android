package com.hilingual.presentation.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryContinueDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onNewClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    if (isVisible) {
        TwoButtonDialog(
            title = "작성 중인 일기가 있어요.",
            description = "임시저장한 일기를 이어 쓰시겠어요?",
            cancelText = "새로 쓰기",
            confirmText = "이어 쓰기",
            onNegative = onNewClick,
            onPositive = onContinueClick,
            onDismiss = onDismiss
        )
    }
}

@Preview
@Composable
private fun DiaryContinueDialogPreview() {
    HilingualTheme {
        DiaryContinueDialog(
            isVisible = true,
            onDismiss = {},
            onNewClick = {},
            onContinueClick = {}
        )
    }
}
