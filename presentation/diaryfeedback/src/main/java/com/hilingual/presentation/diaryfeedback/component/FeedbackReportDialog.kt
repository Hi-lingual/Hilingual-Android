package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedbackReportDialog(
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
) {
    TwoButtonDialog(
        title = "AI 피드백을 신고하시겠어요?",
        description = "신고된 AI 피드백은 확인 후 \n서비스의 운영원칙에 따라 처리됩니다.",
        cancelText = "아니요",
        confirmText = "네, 신고할게요",
        properties = DialogProperties(dismissOnClickOutside = false),
        onNegative = onDismiss,
        onPositive = {
            onReportClick()
            onDismiss()
        },
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedbackReportDialogPreview() {
    HilingualTheme {
        FeedbackReportDialog(
            onDismiss = {},
            onReportClick = {}
       )
    }
}