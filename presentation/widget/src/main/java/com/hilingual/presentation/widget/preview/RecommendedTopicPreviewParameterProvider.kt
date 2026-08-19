package com.hilingual.presentation.widget.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hilingual.presentation.widget.RecommendedTopicUiState
import com.hilingual.presentation.widget.RecommendedTopicWidgetContent
import com.hilingual.presentation.widget.WritingStatus

internal class RecommendedTopicPreviewParameterProvider :
    PreviewParameterProvider<RecommendedTopicUiState> {
    override val values = sequenceOf(
        RecommendedTopicUiState(
            dateLabel = "12월 17일 월",
            topic = "What surprised you today?",
            writingStatus = WritingStatus.UNWRITTEN,
            remainingHours = 25,
        ),
        RecommendedTopicUiState(
            dateLabel = "12월 17일 월",
            topic = "What surprised you today?",
            writingStatus = WritingStatus.WRITTEN,
            remainingHours = null,
        ),
        RecommendedTopicUiState(
            dateLabel = "12월 17일 월",
            topic = null,
            writingStatus = WritingStatus.UNWRITTEN,
            remainingHours = 25,
        ),
    )
}

@Preview(
    name = "Topic 2x2 Light",
    widthDp = 155,
    heightDp = 155,
)
@Preview(
    name = "Topic 2x2 Dark",
    widthDp = 155,
    heightDp = 155,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun RecommendedTopicCompactPreview(
    @PreviewParameter(RecommendedTopicPreviewParameterProvider::class)
    state: RecommendedTopicUiState,
) {
    RecommendedTopicWidgetContent(
        state = state,
        isWide = false,
    )
}

@Preview(
    name = "Topic 4x2 Light",
    widthDp = 329,
    heightDp = 155,
)
@Preview(
    name = "Topic 4x2 Dark",
    widthDp = 329,
    heightDp = 155,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun RecommendedTopicWidePreview(
    @PreviewParameter(RecommendedTopicPreviewParameterProvider::class)
    state: RecommendedTopicUiState,
) {
    RecommendedTopicWidgetContent(
        state = state,
        isWide = true,
    )
}
