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
package com.hilingual.core.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

enum class ErrorImageSize {
    SMALL,
    LARGE
}

@Composable
fun NetworkImage(
    imageUrl: Any?,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    errorImageSize: ErrorImageSize = ErrorImageSize.SMALL,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    val errorImage = remember {
        when (errorImageSize) {
            ErrorImageSize.SMALL -> R.drawable.img_load_fail_small
            ErrorImageSize.LARGE -> R.drawable.img_load_fail_large
        }
    }

    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(R.drawable.img_default_image),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier.clip(shape)

        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = contentScale,
            error = painterResource(errorImage),
            modifier = modifier.clip(shape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkImagePreview() {
    HilingualTheme {
        Row {
            NetworkImage(
                imageUrl = "",
                modifier = Modifier.size(50.dp)
            )
            NetworkImage(
                imageUrl = "",
                shape = RectangleShape,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
