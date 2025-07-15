package com.hilingual.core.designsystem.component.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.component.button.DialogButton

@Composable
fun OneButtonDialog(
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    HilingualBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        content()

        Spacer(modifier = Modifier.height(32.dp))

        DialogButton(
            text = confirmText,
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
