package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedbackCompleteContent(
    diaryId: Long,
    onCloseButtonClick: () -> Unit,
    onShowFeedbackButtonClick: (diaryId: Long) -> Unit
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
            onClick = { onShowFeedbackButtonClick(diaryId) }
        )
    }
}
