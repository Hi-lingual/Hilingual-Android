package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageSelectBottomSheet(
    onDismiss: () -> Unit,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "이미지 선택",
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .noRippleClickable(onClick = onCameraSelected)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_camera_24),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray400
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "카메라로 사진 찍기",
                    style = HilingualTheme.typography.bodySB14,
                    color = HilingualTheme.colors.gray700
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .noRippleClickable(onClick = onGallerySelected)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gallary_24),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray400
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "갤러리에서 선택하기",
                    style = HilingualTheme.typography.bodySB14,
                    color = HilingualTheme.colors.gray700
                )
            }
        }
    }
}
