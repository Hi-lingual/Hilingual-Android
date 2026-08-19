package com.hilingual.presentation.widget.topic

internal data class RecommendedTopicUiState(
    val dateLabel: String,
    val topic: String?,
    val writingStatus: WritingStatus,
    val remainingHours: Int?,
)

internal enum class WritingStatus {
    UNWRITTEN,
    WRITTEN,
}
