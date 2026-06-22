package com.hilingual.presentation.home.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.button.DialogButton
import com.hilingual.core.designsystem.component.dialog.HilingualBasicDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun RecoveryReminderModal(
    isVisible: Boolean,
    onClick: () -> Unit,
    onLaterClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        HilingualBasicDialog(
            onDismiss = onDismiss,
            modifier = modifier,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
        ) {
            Text(
                text = "연속 기록이 끊겼나요?",
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.gray850,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = "광고 한 번 보면 놓쳤던 날짜의 일기를\n다시 작성할 수 있어요.",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Image(
                painter = painterResource(R.drawable.img_modal_return_record_and),
                contentDescription = null,
            )


            Spacer(modifier = Modifier.height(32.dp))

            DialogButton(
                text = "기록 살리기",
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "나중에 살리기",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray400,
                modifier = Modifier.noRippleClickable(onClick = onLaterClick),
            )
        }
    }
}

@Preview
@Composable
private fun RecoveryReminderModalPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            RecoveryReminderModal(
                isVisible = true,
                onClick = {},
                onLaterClick = {},
                onDismiss = {},
            )
        }
    }
}
