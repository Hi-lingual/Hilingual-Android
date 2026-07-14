package com.hilingual.presentation.home.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.trigger.DialogState
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R

@Composable
internal fun NotificationDialog(
    state: DialogState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isVisible) {
        TwoButtonDialog(
            cancelText = "나중에 보기",
            confirmText = "알림 키러가기",
            title = "중요한 소식을 놓치지 마세요.",
            onNegative = onDismiss,
            onPositive = onConfirm,
            onDismiss = onDismiss,
            description = "알림을 키고 누가 당신의 일기에 반응했는지\n바로 확인해 보세요.",
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
            modifier = modifier,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.img_notification_dialog),
                    contentDescription = null,
                    modifier = Modifier.size(279.dp, 125.dp),
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HilingualNotificationDialogPreview() {
    HilingualTheme {
        NotificationDialog(
            state = DialogState(isVisible = true),
            onDismiss = {},
            onConfirm = {},
        )
    }
}
