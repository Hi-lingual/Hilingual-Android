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
package com.hilingual.presentation.feeddiary

import androidx.compose.runtime.Immutable
import com.hilingual.core.designsystem.model.DiaryContent
import com.hilingual.core.designsystem.model.FeedbackContent
import com.hilingual.core.designsystem.model.RecommendExpression
import com.hilingual.presentation.feeddiary.model.ProfileContentUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class FeedDiaryUiState(
    val writtenDate: String = "",
    val isMine: Boolean = false,
    val profileContent: ProfileContentUiModel,
    val diaryContent: DiaryContent = DiaryContent(),
    val feedbackList: ImmutableList<FeedbackContent> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpression> = persistentListOf()
)
