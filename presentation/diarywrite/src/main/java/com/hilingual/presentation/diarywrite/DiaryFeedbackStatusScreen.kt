package com.hilingual.presentation.diarywrite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import com.hilingual.core.designsystem.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.FeedbackUIData
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState

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
internal fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_error_16),
            contentDescription = null,
            tint = HilingualTheme.colors.gray300
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "지금 화면을 나가면, 작성 중인 일기가\n저장되지 않아요",
            color = HilingualTheme.colors.gray300,
            style = HilingualTheme.typography.captionR12,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun CompleteContent(
    onCloseButtonClick: () -> Unit,
    onShowFeedbackButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CloseOnlyTopAppBar(
            onCloseClicked = onCloseButtonClick,
            iconTint = HilingualTheme.colors.black
        )

        HilingualButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 14.dp),
            text = "피드백 보러가기",
            onClick = onShowFeedbackButtonClick
        )
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
            content = { LoadingContent() }
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
                CompleteContent(
                    onCloseButtonClick = {},
                    onShowFeedbackButtonClick = {}
                )
            }
        )
    }
}
