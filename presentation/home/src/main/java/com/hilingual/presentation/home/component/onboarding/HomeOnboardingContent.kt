package com.hilingual.presentation.home.component.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.indicator.HilingualPagerIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

private data class HomeOnboarding(
    val text: String,
    @DrawableRes val image: Int
)

private val onboardingPages = persistentListOf(
    HomeOnboarding(
        "오늘의 일기는\n48시간 동안 작성할 수 있어요.",
        R.drawable.img_onboarding_bottomsheet_1
    ),
    HomeOnboarding(
        "일기를 삭제 한 날에는\n다시 일기를 작성할 수 없어요.",
        R.drawable.img_onboarding_bottomsheet_2
    ),
    HomeOnboarding(
        "작성한 일기는\n커뮤니티에 공유할 수 있어요.",
        R.drawable.img_onboarding_bottomsheet_3
    ),
    HomeOnboarding(
        "일상 속 영어 습관을\n만들 준비가 됐나요?",
        R.drawable.img_onboarding_bottomsheet_4
    )
)

@Composable
internal fun HomeOnboardingContent(
    onStartButtonClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { onboardingPages.size }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 12.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { currentPage ->
            PagerContent(
                text = onboardingPages[currentPage].text,
                image = onboardingPages[currentPage].image
            )
        }

        HilingualPagerIndicator(
            pageCount = onboardingPages.size,
            currentPage = pagerState.currentPage
        )

        HilingualButton(
            text = if (pagerState.currentPage == onboardingPages.lastIndex) "시작하기" else "다음",
            onClick = {
                if (pagerState.currentPage == onboardingPages.lastIndex) {
                    onStartButtonClick()
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
    text: String,
    @DrawableRes image: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = HilingualTheme.typography.headSB20,
            color = HilingualTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier.size(width = 328.dp, height = 164.dp)
        )
    }
}
