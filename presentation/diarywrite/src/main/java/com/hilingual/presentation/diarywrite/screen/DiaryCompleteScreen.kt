package com.hilingual.presentation.diarywrite.screen

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
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun DiaryCompleteScreen(
    paddingValues: PaddingValues,
    diaryId: Long,
    onCloseButtonClick: () -> Unit,
    onShowFeedbackButtonClick: (diaryId: Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CloseOnlyTopAppBar(
                onCloseClicked = onCloseButtonClick,
                iconTint = HilingualTheme.colors.black,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "일기 저장 완료!",
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headSB20,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
                color = HilingualTheme.colors.gray400,
                style = HilingualTheme.typography.headR18,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            HilingualLottieAnimation(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp),
                rawResFile = R.raw.lottie_feedback_complete,
                isInfinite = true,
            )

            Spacer(modifier = Modifier.weight(1f))

            HilingualButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 14.dp),
                text = "피드백 보러가기",
                onClick = { onShowFeedbackButtonClick(diaryId) },
            )
        }
    }
}

@Preview
@Composable
private fun DiaryCompleteScreenPreview() {
    HilingualTheme {
        DiaryCompleteScreen(
            paddingValues = PaddingValues(0.dp),
            diaryId = 0,
            onCloseButtonClick = {},
            onShowFeedbackButtonClick = {},
        )
    }
}
