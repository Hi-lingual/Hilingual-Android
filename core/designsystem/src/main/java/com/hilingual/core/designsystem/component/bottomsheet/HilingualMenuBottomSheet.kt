package com.hilingual.core.designsystem.component.bottomsheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualMenuBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        HilingualBasicBottomSheet(
            onDismiss = onDismiss,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!title.isNullOrBlank()) {
                    Text(
                        text = title,
                        style = HilingualTheme.typography.headB16,
                        color = HilingualTheme.colors.black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                }
                content()
            }
        }
    }
}

@Composable
fun HilingualMenuBottomSheetItem(
    text: String,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = HilingualTheme.colors.gray700,
    iconTintColor: Color = Color.Unspecified
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = iconTintColor
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            style = HilingualTheme.typography.bodySB14,
            color = textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HilingualMenuBottomSheetPreview() {
    HilingualTheme {
        HilingualMenuBottomSheet(
            isVisible = true,
            onDismiss = {},
            title = "이미지 선택"
        ) {
            HilingualMenuBottomSheetItem(
                text = "갤러리에서 선택하기",
                iconResId = R.drawable.ic_search_20,
                onClick = {}
            )
            HilingualMenuBottomSheetItem(
                text = "기본 이미지 적용하기",
                iconResId = R.drawable.ic_search_20,
                onClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HilingualMenuBottomSheetPreview2() {
    HilingualTheme {
        HilingualMenuBottomSheet(
            isVisible = true,
            onDismiss = {}
        ) {
            HilingualMenuBottomSheetItem(
                text = "갤러리에서 선택하기",
                iconResId = R.drawable.ic_search_20,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun HilingualMenuBottomSheetItemPreview() {
    HilingualTheme {
        HilingualMenuBottomSheetItem(
            text = "메뉴 제목",
            iconResId = R.drawable.ic_search_20,
            onClick = {}
        )
    }
}
