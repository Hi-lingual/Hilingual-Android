package com.hilingual.core.designsystem.component.picker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun ProfileImagePicker(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Box(
        modifier = modifier
    ) {
        val imageModifier = Modifier
            .size(120.dp)
            .clip(shape = CircleShape)
            .border(
                shape = CircleShape,
                color = HilingualTheme.colors.gray200,
                width = 1.dp
            )

        if (imageUrl.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.img_default_image),
                contentDescription = null,
                modifier = imageModifier
            )
        } else {
            NetworkImage(
                imageUrl = imageUrl,
                modifier = imageModifier
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_camera_20),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .noRippleClickable(onClick = onClick)
                .background(HilingualTheme.colors.gray100)
                .padding(6.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePickerPreview() {
    HilingualTheme {
        ProfileImagePicker(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePickerWithImageUrlPreview() {
    HilingualTheme {
        ProfileImagePicker(
            onClick = {},
            imageUrl = "https://picsum.photos/120"
        )
    }
}
