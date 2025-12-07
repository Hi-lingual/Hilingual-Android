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
package com.hilingual.core.ui.component.item.voca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.adjBg
import com.hilingual.core.designsystem.theme.adjText
import com.hilingual.core.designsystem.theme.adverbBg
import com.hilingual.core.designsystem.theme.adverbText
import com.hilingual.core.designsystem.theme.hilingualBlue
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.interjectionBg
import com.hilingual.core.designsystem.theme.nounBg
import com.hilingual.core.designsystem.theme.prepositionBg
import com.hilingual.core.designsystem.theme.pronounBg
import com.hilingual.core.designsystem.theme.pronounText
import com.hilingual.core.designsystem.theme.white

@Composable
fun WordPhraseTypeTag(
    phraseType: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = phraseType,
        style = HilingualTheme.typography.captionR12,
        color = PartOfSpeechType.getTypeByText(phraseType)?.textColor ?: HilingualTheme.colors.gray400,
        modifier = modifier
            .clip(CircleShape)
            .background(
                PartOfSpeechType.getTypeByText(phraseType)?.bgColor ?: HilingualTheme.colors.gray100
            )
            .padding(vertical = 2.dp, horizontal = 6.dp)
    )
}

private enum class PartOfSpeechType(
    val displayName: String,
    val bgColor: Color,
    val textColor: Color
) {
    VERB(
        displayName = "동사",
        bgColor = hilingualOrange,
        textColor = white
    ),
    NOUN(
        displayName = "명사",
        bgColor = nounBg,
        textColor = hilingualBlue
    ),
    PRONOUN(
        displayName = "대명사",
        bgColor = pronounBg,
        textColor = pronounText
    ),
    ADJECTIVE(
        displayName = "형용사",
        bgColor = adjBg,
        textColor = adjText
    ),
    ADVERB(
        displayName = "부사",
        bgColor = adverbBg,
        textColor = adverbText
    ),
    PREPOSITION(
        displayName = "전치사",
        bgColor = prepositionBg,
        textColor = hilingualOrange
    ),
    CONJUNCTION(
        displayName = "접속사",
        bgColor = hilingualBlue,
        textColor = white
    ),
    INTERJECTION(
        displayName = "감탄사",
        bgColor = interjectionBg,
        textColor = white
    );

    companion object {
        fun getTypeByText(name: String): PartOfSpeechType? {
            return entries.find { it.displayName == name }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WordPartsPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 8품사
            WordPhraseTypeTag("동사")
            WordPhraseTypeTag("명사")
            WordPhraseTypeTag("대명사")
            WordPhraseTypeTag("형용사")
            WordPhraseTypeTag("부사")
            WordPhraseTypeTag("전치사")
            WordPhraseTypeTag("접속사")
            WordPhraseTypeTag("감탄사")
            // 추가 설명
            WordPhraseTypeTag("숙어")
            WordPhraseTypeTag("속어")
            WordPhraseTypeTag("구")
        }
    }
}
