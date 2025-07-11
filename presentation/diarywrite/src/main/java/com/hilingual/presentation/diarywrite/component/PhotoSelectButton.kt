package com.hilingual.presentation.diarywrite.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun PhotoSelectButton(
    onPhotoSelectClick: () -> Unit,
    onDeleteClick: () -> Unit,
    selectedImgUri: Uri? = null
) {
    Box(
        modifier = Modifier.size(width = 88.dp, height = 89.dp)
    ) {
        if (selectedImgUri != null) {
            NetworkImage(
                imageUrl = selectedImgUri,
                shape = RectangleShape,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(8.dp))
            )
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.TopEnd)
                    .noRippleClickable(onClick = onDeleteClick),
                imageVector = ImageVector.vectorResource(R.drawable.ic_delete_circle_22),
                contentDescription = null,
                tint = Color.Unspecified
            )
        } else {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(8.dp))
                    .background(HilingualTheme.colors.gray100)
                    .noRippleClickable(onClick = onPhotoSelectClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_camera_20),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray300
                )
            }
        }
    }
}

@Preview
@Composable
private fun PhotoSelectButtonPreview() {
    val imgUriState = remember { mutableStateOf<Uri?>("".toUri()) }

    HilingualTheme {
        PhotoSelectButton(
            onPhotoSelectClick = {
                // Preview에서는 초기 상태의 버튼을 클릭했을 때 이미지가 자동으로 삽입된다고 가정해 테스트 진행함 (실제 UI에서는 바텀시트로 연결됨)
                imgUriState.value = "".toUri()
            },
            onDeleteClick = { imgUriState.value = null },
            selectedImgUri = imgUriState.value
        )
    }
}
