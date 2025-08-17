/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.diaryfeedback.model.DiaryContentUiModel
import com.hilingual.presentation.diaryfeedback.model.FeedbackContentUiModel
import com.hilingual.presentation.diaryfeedback.model.RecommendExpressionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class DiaryFeedbackUiState(
    val writtenDate: String = "",
    val isPublished: Boolean = false,
    val diaryContent: DiaryContentUiModel = DiaryContentUiModel(),
    val feedbackList: ImmutableList<FeedbackContentUiModel> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpressionUiModel> = persistentListOf()
)
