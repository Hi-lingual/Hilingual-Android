package com.hilingual.presentation.diarywrite

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState
import com.hilingual.presentation.diarywrite.component.FeedbackCompleteContent
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
import com.hilingual.presentation.diarywrite.component.FeedbackUIData

@Composable
internal fun DiaryFeedbackStatusScreen(
    paddingValues: PaddingValues,
    state: FeedbackUIData,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white)
    ) {
        TextAndLottie(
            modifier = Modifier.align(Alignment.Center),
            title = state.title,
            description = state.description,
            lottieRawRes = state.lottieRawRes,
            lottieRawResHeightDp = state.lottieRawResHeightDp
        )

        content()
    }
}

@Composable
private fun TextAndLottie(
    title: String,
    description: String,
    lottieRawRes: Int,
    lottieRawResHeightDp: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = HilingualTheme.colors.gray850,
            style = HilingualTheme.typography.headB20,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = description,
            color = HilingualTheme.colors.gray400,
            style = HilingualTheme.typography.bodyR18,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        HilingualLottieAnimation(
            modifier = Modifier
                .width(200.dp)
                .height(lottieRawResHeightDp),
            rawResFile = lottieRawRes,
            isInfinite = true
        )
    }
}

@Preview
@Composable
private fun DiaryFeedbackLoadingStatusScreenPreview() {
    HilingualTheme {
        DiaryFeedbackStatusScreen(
            paddingValues = PaddingValues(0.dp),
            state = DiaryFeedbackState.Loading.data ?: FeedbackUIData(),
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
            state = DiaryFeedbackState.Complete.data ?: FeedbackUIData(),
            content = {
                FeedbackCompleteContent(
                    onCloseButtonClick = {},
                    onShowFeedbackButtonClick = {}
                )
            }
        )
    }
}
