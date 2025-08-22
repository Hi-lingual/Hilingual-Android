/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.home.component.footer

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
import com.hilingual.core.designsystem.component.dialog.diary.DiaryDeleteDialog
import com.hilingual.core.designsystem.component.dialog.diary.DiaryPublishDialog
import com.hilingual.core.designsystem.component.dialog.diary.DiaryUnpublishDialog
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
            if (isPublished) {
                DiaryUnpublishDialog(
                    isVisible = true,
                    onDismiss = { publishDialogVisible = false },
                    onPrivateClick = {
                        onUnpublishClick()
                        publishDialogVisible = false
                    }
                )
            } else {
                DiaryPublishDialog(
                    isVisible = true,
                    onDismiss = { publishDialogVisible = false },
                    onPostClick = {
                        onPublishClick()
                        publishDialogVisible = false
                    }
                )
            }
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
