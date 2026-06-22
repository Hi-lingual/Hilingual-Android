package com.hilingual.presentation.home.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.dialog.OneButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun RecoveryNoticeModal(
    isVisible: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        OneButtonDialog(
            confirmText = "확인했습니다",
            onConfirm = onClick,
            onDismiss = onDismiss,
            modifier = modifier,
        ) {
            Text(
                text = "이제 끊긴 기록을 되살릴 수 있어요!",
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.gray850,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                text = "미처 작성하지 못한 날이 있다면?\n광고 한 편 보고 끊긴 기록을 살려보세요.",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp),
            )
            Image(
                painter = painterResource(R.drawable.img_modal_update_and),
                contentDescription = null,
                modifier = Modifier.aspectRatio(280 / 180f),
            )
        }
    }
}

@Preview
@Composable
private fun RecoveryNoticeModalPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            RecoveryNoticeModal(
                isVisible = true,
                onClick = {},
                onDismiss = {},
            )
        }
    }
}
