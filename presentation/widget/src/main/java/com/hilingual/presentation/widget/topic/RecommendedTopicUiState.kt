package com.hilingual.presentation.widget.topic

internal data class RecommendedTopicUiState(
    val dateLabel: String,
    val topic: String?,
    val writingStatus: WritingStatus,
    val remainingHours: Int?,
) {
    companion object {
        val Fake = RecommendedTopicUiState(
            dateLabel = "12월 17일 월",
            topic = "What surprised you today?",
            writingStatus = WritingStatus.UNWRITTEN,
            remainingHours = 25,
        )

        val WrittenFake = RecommendedTopicUiState(
            dateLabel = "12월 17일 월",
            topic = "What surprised you today?",
            writingStatus = WritingStatus.WRITTEN,
            remainingHours = null,
        )
    }
}

internal enum class WritingStatus {
    UNWRITTEN,
    WRITTEN,
}
