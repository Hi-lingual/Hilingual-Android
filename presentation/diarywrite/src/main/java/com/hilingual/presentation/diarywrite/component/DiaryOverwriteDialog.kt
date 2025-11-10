package com.hilingual.presentation.diarywrite.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryOverwriteDialog(
    onDismiss: () -> Unit,
    onNoClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    TwoButtonDialog(
        title = "이미 임시저장한 일기가 있어요.",
        description = "일자 당 하나의 일기만 임시저장할 수 있어요.\n임시저장한 일기에 덮어쓰시겠어요?",
        cancelText = "아니요",
        confirmText = "덮어쓰기",
        onNegative = onNoClick,
        onPositive = onCancelClick,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun DiaryOverwriteDialogPreview() {
    HilingualTheme {
        DiaryOverwriteDialog(
            onDismiss = {},
            onNoClick = {},
            onCancelClick = {}
        )
    }
}
