package com.hilingual.presentation.diarywrite.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun PhotoSelectButton(
    selectedImgUri: Uri? = null,
    onImgSelected: (Uri?) -> Unit
) {
    var isGalleryLaunching by remember { mutableStateOf(false) }

    val photoSelectLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        isGalleryLaunching = false

        if (uri != null) {
            onImgSelected(uri)
        }
    }

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
                    .noRippleClickable {
                        onImgSelected(null)
                    },
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
                    .noRippleClickable(onClick = {
                        // TODO: flow나 channel 등으로 리팩토링하기
                        if (!isGalleryLaunching) {
                            isGalleryLaunching = true
                            photoSelectLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }),
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
    val imgUriState = remember { mutableStateOf<Uri?>(null) }

    HilingualTheme {
        PhotoSelectButton(
            selectedImgUri = imgUriState.value,
            onImgSelected = { newUri ->
                imgUriState.value = newUri
            }
        )
    }
}
