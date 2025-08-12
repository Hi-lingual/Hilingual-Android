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
