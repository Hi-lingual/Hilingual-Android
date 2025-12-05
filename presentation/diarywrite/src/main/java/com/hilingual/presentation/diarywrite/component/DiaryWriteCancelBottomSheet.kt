package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryWriteCancelBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onTempSaveClick: () -> Unit
) {
    HilingualBasicBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "일기 작성을 취소하시겠어요?",
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.hilingualBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "임시저장하지 않은 내용은 모두 사라져요!",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(24.dp))

            DiaryWriteActionRow(
                iconRes = R.drawable.ic_cancel_24,
                text = "작성취소",
                textColor = HilingualTheme.colors.alertRed,
                onClick = onCancelClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            DiaryWriteActionRow(
                iconRes = R.drawable.ic_save_24,
                text = "임시저장",
                textColor = HilingualTheme.colors.gray700,
                onClick = onTempSaveClick
            )
        }
    }
}

@Composable
private fun DiaryWriteActionRow(
    iconRes: Int,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .noRippleClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = HilingualTheme.typography.bodyM14,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun DiaryWriteCancelBottomSheetPreview() {
    HilingualTheme {
        DiaryWriteCancelBottomSheet(
            isVisible = true,
            onDismiss = {},
            onCancelClick = {},
            onTempSaveClick = {}
        )
    }
}
