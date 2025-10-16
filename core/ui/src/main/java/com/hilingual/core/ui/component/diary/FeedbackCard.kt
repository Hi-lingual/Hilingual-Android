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
package com.hilingual.core.ui.component.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedbackCard(
    originalText: String,
    feedbackText: String,
    explain: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .padding(12.dp)
    ) {
        FeedbackTopContent(
            originalText = originalText,
            feedbackText = feedbackText
        )

        HorizontalDivider(
            thickness = Dp.Hairline
        )

        Text(
            text = explain,
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.hilingualBlack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FeedbackTopContent(
    originalText: String,
    feedbackText: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        FeedbackSentence(
            text = originalText,
            isFeedback = false
        )

        FeedbackSentence(
            text = feedbackText,
            isFeedback = true
        )
    }
}

@Composable
private fun FeedbackSentence(
    text: String,
    isFeedback: Boolean,
    modifier: Modifier = Modifier
) {
    val (iconRes, color, style) = if (isFeedback) {
        Triple(
            R.drawable.chip_feedback_card_ai,
            HilingualTheme.colors.hilingualOrange,
            HilingualTheme.typography.bodyM16
        )
    } else {
        Triple(
            R.drawable.chip_feedback_card_me,
            HilingualTheme.colors.gray700,
            HilingualTheme.typography.bodyR16
        )
    }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = text,
            style = style,
            color = color
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun FeedbackCardPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FeedbackCard(
                originalText = "i’m drinking milk because I easily get stomachache",
                feedbackText = "I’m drinking milk because I get stomachaches easily",
                explain = "a stomachache처럼 가산명사는 ~게 작성하는게 맞는 표현이에요. ‘easily’의 어순을 문장 마지막에 작성하여 더 정확해졌어요."
            )
            FeedbackCard(
                originalText = "I was planning to arrive it here around 13:30",
                feedbackText = "I was planning to arrive here around 1:30 p.m",
                explain = "arrive는 자동사이기 때문에 직접 목적어 ‘it’을 쓸 수 없어요. ‘arrive at the station’, ‘arrive here’처럼 써야 맞는 표현이에요!"
            )
        }
    }
}
