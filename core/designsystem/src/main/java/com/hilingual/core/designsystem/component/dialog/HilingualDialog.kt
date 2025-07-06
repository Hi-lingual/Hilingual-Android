package com.hilingual.core.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = HilingualTheme.colors.white,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(top = 34.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            Text(
                text = title,
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.gray850
            )
            Text(
                text = description,
                style = HilingualTheme.typography.captionM12,
                color = HilingualTheme.colors.gray400
            )
            content()
        }
    }
}

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
    HilingualDialog(
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
    HilingualDialog(
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

@Composable
private fun DialogButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isFilled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (isFilled)
                    HilingualTheme.colors.hilingualOrange
                else
                    HilingualTheme.colors.gray100
            )
            .padding(vertical = 14.dp)
            .noRippleClickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HilingualTheme.typography.bodySB16,
            color = if (isFilled)
                HilingualTheme.colors.white
            else
                HilingualTheme.colors.gray400
        )
    }
}


@Preview
@Composable
private fun HilingualDialogPreview() {
    HilingualTheme {
        HilingualDialog(
            title = "AI 피드백을 신고하시겠어요?",
            description = "신고된 AI 피드백은 확인 후 서비스의 운영원칙...",
            onDismiss = {}
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = HilingualTheme.colors.gray100)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "취소",
                        style = HilingualTheme.typography.bodySB16,
                        color = HilingualTheme.colors.gray400
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HilingualTheme.colors.hilingualOrange)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "확인",
                        style = HilingualTheme.typography.bodySB16,
                        color = HilingualTheme.colors.white
                    )
                }
            }
        }
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
