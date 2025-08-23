package com.hilingual.presentation.mypage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun WithdrawDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (!isVisible) {
        TwoButtonDialog(
            title = "정말 계정을 삭제하시겠어요?",
            description = "회원 탈퇴 시 작성한 일기를 비롯한 계정 정보는 \n" +
                    "영원히 삭제돼요. 정말 삭제를 원하시나요?",
            cancelText = "아니요",
            confirmText = "삭제하기",
            onNegative = onDismiss,
            onPositive = onDeleteClick,
            onDismiss = onDismiss
        )
    }
}

@Preview
@Composable
private fun WithdrawDialogPreview() {
    HilingualTheme {
        WithdrawDialog(
            isVisible = true,
            onDismiss = {},
            onDeleteClick = {}
        )
    }
}
