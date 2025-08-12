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
package com.hilingual.presentation.voca.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

internal enum class VocaEmptyCardType(
    val text: String,
    @DrawableRes val imageRes: Int
) {
    NOT_ADD(
        text = "아직 단어가 추가되지 않았어요.",
        imageRes = DesignSystemR.drawable.img_word
    ),
    NOT_SEARCH(
        text = "검색 결과가 없습니다.",
        imageRes = DesignSystemR.drawable.img_search
    )
}

@Composable
internal fun VocaEmptyCard(
    type: VocaEmptyCardType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Image(
            modifier = Modifier
                .size(width = 200.dp, height = 140.dp),
            painter = painterResource(id = type.imageRes),
            contentDescription = null
        )
        Text(
            text = type.text,
            style = HilingualTheme.typography.headM18,
            color = HilingualTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}

private class VocaEmptyCardPreviewParameterProvider :
    PreviewParameterProvider<VocaEmptyCardType> {
    override val values: Sequence<VocaEmptyCardType>
        get() = VocaEmptyCardType.entries.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun VocaEmptyCardPreview(
    @PreviewParameter(VocaEmptyCardPreviewParameterProvider::class)
    type: VocaEmptyCardType
) {
    HilingualTheme {
        VocaEmptyCard(type = type)
    }
}
