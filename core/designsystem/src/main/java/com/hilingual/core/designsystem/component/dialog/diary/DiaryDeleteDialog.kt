package com.hilingual.core.designsystem.component.dialog.diary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun DiaryDeleteDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "일기를 삭제하시겠어요?",
            description = "작성한 일기를 삭제한 날짜에는 \n" +
                "다시 일기를 작성할 수 없어요.",
            cancelText = "아니요",
            confirmText = "삭제하기",
            onNegative = onDismiss,
            onPositive = onDeleteClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryDeletePreview() {
    HilingualTheme {
        DiaryDeleteDialog(
            isVisible = true,
            onDismiss = {},
            onDeleteClick = {}
        )
    }
}
