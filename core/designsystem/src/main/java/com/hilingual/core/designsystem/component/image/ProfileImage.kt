package com.hilingual.core.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.hilingual.core.designsystem.R

@Composable
fun ProfileImage(
    imageUrl: String?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.img_default_image),
            contentDescription = null,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    } else {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = modifier.size(size)
        )
    }
}
