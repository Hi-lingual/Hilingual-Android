package com.hilingual.core.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.hilingualBlue
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.interjectionBg
import com.hilingual.core.designsystem.theme.nounBg
import com.hilingual.core.designsystem.theme.prepositionBg
import com.hilingual.core.designsystem.theme.prepositionText
import com.hilingual.core.designsystem.theme.pronounBg
import com.hilingual.core.designsystem.theme.pronounText
import com.hilingual.core.designsystem.theme.white

@Composable
fun WordPartsTag(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = HilingualTheme.typography.captionM12,
        color = PartOfSpeechType.getTypeByText(title)?.textColor ?: gray400,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                PartOfSpeechType.getTypeByText(title)?.bgColor ?: HilingualTheme.colors.gray100
            )
            .padding(vertical = 2.dp, horizontal = 6.dp)
    )
}

enum class PartOfSpeechType(val displayName: String, val bgColor: Color, val textColor: Color) {
    VERB("동사", hilingualOrange, white),
    NOUN("명사", nounBg, hilingualBlue),
    PRONOUN("대명사", pronounBg, pronounText),
    ADJECTIVE("형용사", adjBg, adjText),
    ADVERB("부사", adverbBg, adverbText),
    PREPOSITION("전치사", prepositionBg, prepositionText),
    CONJUNCTION("접속사", hilingualBlue, white),
    INTERJECTION("감탄사", interjectionBg, white);

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
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // 8품사
            WordPartsTag("명사")
            WordPartsTag("대명사")
            WordPartsTag("동사")
            WordPartsTag("형용사")
            WordPartsTag("부사")
            WordPartsTag("전치사")
            WordPartsTag("접속사")
            WordPartsTag("감탄사")
            // 서브 품사
            WordPartsTag("숙어")
            WordPartsTag("속어")
            WordPartsTag("구")
        }
    }
}