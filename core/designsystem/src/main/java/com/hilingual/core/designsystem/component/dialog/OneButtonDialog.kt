package com.hilingual.core.designsystem.component.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun OneButtonDialog(
    title: String,
    description: String,
    confirmText: String,
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
        DialogButton(
            text = confirmText,
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun OneButtonDialogPreview() {
    HilingualTheme {
        OneButtonDialog(
            title = "회원 탈퇴하시겠어요?",
            description = "탈퇴 시 모든 정보가 삭제되며 복구되지 않아요.",
            confirmText = "확인",
            onConfirm = {},
            onDismiss = {}
        )
    }
}