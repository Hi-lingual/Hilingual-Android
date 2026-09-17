/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
