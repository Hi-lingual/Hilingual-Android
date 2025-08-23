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
package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualProfileImageBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDefaultImageClick: () -> Unit,
    onGalleryImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualMenuBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        title = "이미지 선택",
        modifier = modifier
    ) {
        HilingualMenuBottomSheetItem(
            text = "기본 이미지로 변경하기",
            iconResId = R.drawable.ic_image_24,
            onClick = onDefaultImageClick
        )

        HilingualMenuBottomSheetItem(
            text = "갤러리에서 선택하기",
            iconResId = R.drawable.ic_gallary_24,
            onClick = onGalleryImageClick
        )
    }
}

@Preview
@Composable
private fun HilingualImagePickerBottomSheetPreview() {
    HilingualTheme {
        HilingualProfileImageBottomSheet(
            isVisible = true,
            onDismiss = {},
            onDefaultImageClick = {},
            onGalleryImageClick = {}
        )
    }
}
