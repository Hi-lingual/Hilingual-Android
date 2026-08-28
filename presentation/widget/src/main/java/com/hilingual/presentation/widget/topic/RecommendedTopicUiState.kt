package com.hilingual.presentation.widget.topic

import com.hilingual.core.common.util.toLocalDateOrNull
import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate

internal data class RecommendedTopicUiState(
    val date: LocalDate = LocalDate.now(),
    val topic: String?,
    val writingStatus: WritingStatus,
) {
    companion object {
        fun from(model: WidgetTopicModel): RecommendedTopicUiState = RecommendedTopicUiState(
            date = model.date.toLocalDateOrNull() ?: LocalDate.now(),
            topic = model.topicEn,
            writingStatus = if (model.isWrittenToday == true) {
                WritingStatus.WRITTEN
            } else if (model.isWrittenToday == false) {
                WritingStatus.UNWRITTEN
            } else {
                WritingStatus.UNKNOWN
            },
        )

        fun unavailable(date: LocalDate = LocalDate.now()): RecommendedTopicUiState = RecommendedTopicUiState(
            date = date,
            topic = null,
            writingStatus = WritingStatus.UNKNOWN,
        )

        val Fake = RecommendedTopicUiState(
            topic = "What surprised you today?",
            writingStatus = WritingStatus.UNWRITTEN,
        )

        val WrittenFake = RecommendedTopicUiState(
            topic = "What surprised you today?",
            writingStatus = WritingStatus.WRITTEN,
        )
    }
}

internal enum class WritingStatus {
    UNWRITTEN,
    WRITTEN,
    UNKNOWN,
}
