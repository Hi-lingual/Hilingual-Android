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
package com.hilingual.presentation.diaryfeedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

// TODO: sharedTransition 사용
@Composable
internal fun ModalImage(
    imageUrl: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.black)
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            shape = RectangleShape,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
                .align(Alignment.Center)
        )

        CloseOnlyTopAppBar(
            onCloseClicked = onBackClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(HilingualTheme.colors.black)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoDetailPreview() {
    HilingualTheme {
        ModalImage(
            imageUrl = "",
            onBackClick = {}
        )
    }
}
