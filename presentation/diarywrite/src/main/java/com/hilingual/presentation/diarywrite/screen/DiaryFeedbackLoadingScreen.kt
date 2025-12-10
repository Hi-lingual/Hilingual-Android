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
package com.hilingual.presentation.diarywrite.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.AnimatedLoadingLottie
import com.hilingual.presentation.diarywrite.component.AnimatedLoadingText
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DiaryFeedbackLoadingScreen(
    lottieCompositions: ImmutableList<LottieComposition?>,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "일기 저장 중...",
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headSB20,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedLoadingText(
                texts = persistentListOf(
                    "피드백을 요청하고 있어요.",
                    "오늘 하루도 수고했어요!",
                    "발전하는 모습이 멋져요."
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedLoadingLottie(
                lottieCompositions = lottieCompositions,
                height = 194.dp
            )
        }

        FeedbackLoadingContent()
    }
}

@Preview
@Composable
private fun DiaryFeedbackLoadingScreenPreview() {
    HilingualTheme {
        DiaryFeedbackLoadingScreen(
            lottieCompositions = persistentListOf(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}
