package com.hilingual.core.designsystem.component.dialog.diary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun DiaryUnpublishDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onPrivateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "영어 일기를 비공개 하시겠어요?",
            description = "비공개로 전환 시, 해당 일기는\n피드 활동에서 확인할 수 없어요.",
            cancelText = "아니요",
            confirmText = "비공개하기",
            onNegative = onDismiss,
            onPositive = onPrivateClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryUnpublishPreview() {
    HilingualTheme {
        DiaryUnpublishDialog(
            isVisible = true,
            onDismiss = {},
            onPrivateClick = { }
        )
    }
}
