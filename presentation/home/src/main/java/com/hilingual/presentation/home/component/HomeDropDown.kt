package com.hilingual.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.component.dialog.diary.DiaryDeleteDialog
import com.hilingual.core.designsystem.component.dropdown.HilingualBasicDropdownMenu
import com.hilingual.core.designsystem.component.dropdown.HilingualDropdownMenuItem
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun HomeDropDownMenu(
    isExpanded: Boolean,
    isPublished: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onPublishClick: () -> Unit,
    onUnpublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var publishDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }

    HilingualBasicDropdownMenu(
        isExpanded = isExpanded,
        modifier = modifier,
        onExpandedChange = onExpandedChange
    ) {
        HilingualDropdownMenuItem(
            text = if (isPublished) "비공개하기" else "게시하기",
            iconResId = if (isPublished) R.drawable.ic_hide_24 else R.drawable.ic_upload_24,
            onClick = {
                publishDialogVisible = true
                onExpandedChange(false)
            }
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = HilingualTheme.colors.gray200
        )
        HilingualDropdownMenuItem(
            text = "삭제하기",
            iconResId = R.drawable.ic_delete_24,
            onClick = {
                deleteDialogVisible = true
                onExpandedChange(false)
            },
            textColor = HilingualTheme.colors.alertRed
        )
    }

    when {
        deleteDialogVisible -> {
            DiaryDeleteDialog(
                isVisible = true,
                onDismiss = { deleteDialogVisible = false },
                onDeleteClick = {
                    onDeleteClick()
                    deleteDialogVisible = false
                }
            )
        }

        publishDialogVisible -> {
            val title = if (isPublished) "영어 일기를 비공개 하시겠어요?" else "영어 일기를 게시하시겠어요?"
            val description =
                if (isPublished) "비공개로 전환 시, 해당 일기는\n피드 활동에서 확인할 수 없어요." else "공유된 일기는 모든 유저에게 게시되며, \n피드에서 확인하실 수 있어요."

            val confirmText = if (isPublished) "비공개하기" else "게시하기"
            val onConfirm = {
                if (isPublished) onUnpublishClick() else onPublishClick()
                publishDialogVisible = false
            }

            TwoButtonDialog(
                modifier = modifier,
                title = title,
                description = description,
                cancelText = "아니요",
                confirmText = confirmText,
                onNegative = { publishDialogVisible = false },
                onPositive = onConfirm,
                onDismiss = { publishDialogVisible = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDropDownMenuPreview() {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpanded1 by remember { mutableStateOf(false) }
    HilingualTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeDropDownMenu(
                isExpanded = isExpanded,
                isPublished = false,
                onExpandedChange = { isExpanded = it },
                onDeleteClick = {},
                onPublishClick = {},
                onUnpublishClick = {}
            )

            HomeDropDownMenu(
                isExpanded = isExpanded1,
                isPublished = true,
                onExpandedChange = { isExpanded1 = it },
                onDeleteClick = {},
                onPublishClick = {},
                onUnpublishClick = {}
            )
        }
    }
}
