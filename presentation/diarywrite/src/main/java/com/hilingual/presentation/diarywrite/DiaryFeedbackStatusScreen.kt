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
package com.hilingual.presentation.diarywrite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.FeedbackCompleteContent
import com.hilingual.presentation.diarywrite.component.FeedbackFailureContent
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
import com.hilingual.presentation.diarywrite.component.FeedbackMedia
import com.hilingual.presentation.diarywrite.component.FeedbackUIData
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun DiaryFeedbackStatusScreen(
    paddingValues: PaddingValues,
    uiData: FeedbackUIData,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white)
    ) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiData.title,
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headB20,
                textAlign = TextAlign.Center
            )

            if (uiData.description != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiData.description,
                    color = HilingualTheme.colors.gray400,
                    style = HilingualTheme.typography.bodyR18,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (val media = uiData.media) {
                is FeedbackMedia.Lottie -> {
                    HilingualLottieAnimation(
                        modifier = Modifier
                            .width(200.dp)
                            .height(media.heightDp),
                        rawResFile = media.resId,
                        isInfinite = true
                    )
                }

                is FeedbackMedia.Image -> {
                    Image(
                        modifier = Modifier
                            .width(200.dp)
                            .height(media.heightDp),
                        painter = painterResource(media.resId),
                        contentDescription = null
                    )
                }
            }
        }

        content()
    }
}

@Preview
@Composable
private fun DiaryFeedbackLoadingStatusScreenPreview() {
    HilingualTheme {
        DiaryFeedbackStatusScreen(
            paddingValues = PaddingValues(0.dp),
            uiData = FeedbackUIData(
                title = "일기 저장 중..",
                description = "피드백을 요청하고 있어요.",
                media = FeedbackMedia.Lottie(
                    resId = R.raw.lottie_feedback_loading,
                    heightDp = 194.dp
                )
            ),
            content = { FeedbackLoadingContent() }
        )
    }
}

@Preview
@Composable
private fun DiaryFeedbackCompleteStatusScreenPreview() {
    HilingualTheme {
        DiaryFeedbackStatusScreen(
            paddingValues = PaddingValues(0.dp),
            uiData = FeedbackUIData(
                title = "일기 저장 완료!",
                description = "펜펜이가 틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
                media = FeedbackMedia.Lottie(
                    resId = R.raw.lottie_feedback_complete,
                    heightDp = 180.dp
                )
            ),
            content = {
                FeedbackCompleteContent(
                    diaryId = 0,
                    onCloseButtonClick = {},
                    onShowFeedbackButtonClick = {}
                )
            }
        )
    }
}

@Preview
@Composable
private fun DiaryFeedbackFailureStatusScreenPreview() {
    HilingualTheme {
        DiaryFeedbackStatusScreen(
            paddingValues = PaddingValues(0.dp),
            uiData = FeedbackUIData(
                title = "앗! 일시적인 오류가 발생했어요.",
                media = FeedbackMedia.Image(
                    resId = DesignSystemR.drawable.img_error,
                    heightDp = 175.dp
                )
            ),
            content = {
                FeedbackFailureContent(
                    onCloseButtonClick = {},
                    onRequestAgainButtonClick = {}
                )
            }
        )
    }
}
