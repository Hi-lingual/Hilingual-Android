package com.hilingual.presentation.diarywrite.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.FeedbackFailureContent
import com.hilingual.presentation.diarywrite.component.FeedbackMedia
import com.hilingual.presentation.diarywrite.component.FeedbackUIData
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun DiaryFailureScreen(
    paddingValues: PaddingValues,
    onCloseButtonClick: () -> Unit,
    onRequestAgainButtonClick: () -> Unit,
) {
    DiaryFeedbackStatusScreen(
        paddingValues = paddingValues,
        uiData = FeedbackUIData(
            title = "앗! 일시적인 오류가 발생했어요.",
            media = FeedbackMedia.Image(
                resId = DesignSystemR.drawable.img_error,
                heightDp = 175.dp,
            ),
        ),
        content = {
            FeedbackFailureContent(
                onCloseButtonClick = onCloseButtonClick,
                onRequestAgainButtonClick = onRequestAgainButtonClick,
            )
        },
    )
}

@Preview
@Composable
private fun DiaryFailureScreenPreview() {
    HilingualTheme {
        DiaryFailureScreen(
            paddingValues = PaddingValues(0.dp),
            onCloseButtonClick = {},
            onRequestAgainButtonClick = {},
        )
    }
}
