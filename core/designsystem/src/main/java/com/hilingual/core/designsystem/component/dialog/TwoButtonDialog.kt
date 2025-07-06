package com.hilingual.core.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TwoButtonDialog(
    title: String,
    description: String,
    cancelText: String,
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties()
) {
    BasicHilingualDialog(
        title = title,
        description = description,
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            DialogButton(
                text = cancelText,
                onClick = onCancel,
                isFilled = false,
                modifier = Modifier.weight(1f)
            )
            DialogButton(
                text = confirmText,
                onClick = onConfirm,
                isFilled = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TwoButtonDialogPreview() {
    HilingualTheme {
        TwoButtonDialog(
            title = "AI 피드백을 신고하시겠어요?",
            description = "신고된 AI 피드백은 확인 후 서비스 운영원칙...",
            cancelText = "취소",
            confirmText = "확인",
            onCancel = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}
