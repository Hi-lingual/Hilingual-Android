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
package com.hilingual.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.indicator.HilingualPagerIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.onboarding.model.OnboardingContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

private val onboardingPages: ImmutableList<OnboardingContent> = persistentListOf(
    OnboardingContent(
        text = "48시간 동안 작성하는\n꾸준한 영어일기",
        highlightedText = "48시간",
        image = R.drawable.img_onboarding_1
    ),
    OnboardingContent(
        text = "한 줄도, 한국어도, 사진도\n괜찮은 일기 작성",
        highlightedText = "일기 작성",
        image = R.drawable.img_onboarding_2
    ),
    OnboardingContent(
        text = "영어일기에 최적화 된\n간편한 AI 피드백",
        highlightedText = "AI 피드백",
        image = R.drawable.img_onboarding_3
    ),
    OnboardingContent(
        text = "일기를 공유하며\n더불어 성장하는 피드",
        highlightedText = "일기를 공유",
        image = R.drawable.img_onboarding_4
    )
)

@Composable
internal fun OnboardingRoute(
    paddingValues: PaddingValues,
    navigateToAuth: () -> Unit
) {
    OnboardingScreen(
        paddingValues = paddingValues,
        onOnboardingCompleted = navigateToAuth
    )
}

@Composable
private fun OnboardingScreen(
    paddingValues: PaddingValues,
    onOnboardingCompleted: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { onboardingPages.size }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .statusBarColor(HilingualTheme.colors.white)
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(bottom = 49.dp)
    ) {
        Text(
            text = "건너뛰기",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray400,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .noRippleClickable(onClick = onOnboardingCompleted)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .align(Alignment.End)
        )

        Spacer(modifier = Modifier.weight(24f))

        HorizontalPager(
            state = pagerState
        ) { currentPage ->
            PagerContent(
                onboardingContent = onboardingPages[currentPage]
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        HilingualPagerIndicator(
            pageCount = onboardingPages.size,
            currentPage = pagerState.currentPage
        )

        Spacer(modifier = Modifier.weight(60f))

        HilingualButton(
            text = "다음",
            onClick = {
                if (pagerState.currentPage == onboardingPages.lastIndex) {
                    onOnboardingCompleted()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun PagerContent(
    onboardingContent: OnboardingContent
) {
    val fullText = onboardingContent.text
    val highlightedText = onboardingContent.highlightedText

    val annotatedString = buildAnnotatedString {
        append(fullText)

        var startIndex = fullText.indexOf(highlightedText)

        while (startIndex >= 0) {
            addStyle(
                style = SpanStyle(color = HilingualTheme.colors.hilingualOrange),
                start = startIndex,
                end = startIndex + highlightedText.length
            )

            startIndex = fullText.indexOf(
                highlightedText, startIndex + highlightedText.length
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = annotatedString,
            style = HilingualTheme.typography.headSB20,
            color = HilingualTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(onboardingContent.image),
            contentDescription = null,
            modifier = Modifier.size(360.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    HilingualTheme {
        OnboardingScreen(
            paddingValues = PaddingValues()
        ) { }
    }
}
