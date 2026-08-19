/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.presentation.voca.review.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun ReviewResultContent(
    paddingValues: PaddingValues,
    @DrawableRes imageRes: Int,
    imageModifier: Modifier,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
    onCloseClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
    ) {
        if (onCloseClick != null) {
            HilingualBasicTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_close_24),
                        contentDescription = null,
                        tint = HilingualTheme.colors.black,
                        modifier = Modifier.noRippleClickable(onClick = onCloseClick),
                    )
                },
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = imageModifier,
                )
                Text(
                    text = message,
                    style = HilingualTheme.typography.headSB18,
                    color = HilingualTheme.colors.hilingualBlack,
                    textAlign = TextAlign.Center,
                )
            }
        }

        HilingualButton(
            text = buttonText,
            onClick = onButtonClick,
            enableProvider = { !isSaving },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewResultContentPreview() {
    HilingualTheme {
        ReviewResultContent(
            paddingValues = PaddingValues(0.dp),
            imageRes = DesignSystemR.drawable.img_review_finish,
            imageModifier = Modifier.height(180.dp),
            message = "단어를 모두 복습했어요.\n노력하는 당신이 대단해요!",
            buttonText = "완료",
            onButtonClick = {},
            isSaving = false,
        )
    }
}
