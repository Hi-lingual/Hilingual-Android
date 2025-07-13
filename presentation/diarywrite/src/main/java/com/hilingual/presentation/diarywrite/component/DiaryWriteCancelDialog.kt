package com.hilingual.presentation.diarywrite.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryWriteCancelDialog(
    onDismiss: () -> Unit,
    onNoClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    TwoButtonDialog(
        title = "일기 작성을 취소하시겠어요?",
        description = "지금 나가면 작성한 내용이 모두 사라져요!",
        cancelText = "아니요",
        confirmText = "네, 취소할게요",
        onNegative = onNoClick,
        onPositive = onCancelClick,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun DiaryWriteCancelDialogPreview() {
    HilingualTheme {
        DiaryWriteCancelDialog(
            onDismiss = {},
            onNoClick = {},
            onCancelClick = {}
        )
    }
}
