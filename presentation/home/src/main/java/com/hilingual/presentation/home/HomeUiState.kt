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
package com.hilingual.presentation.home

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.DiaryThumbnailUiModel
import com.hilingual.presentation.home.model.TodayTopicUiModel
import com.hilingual.presentation.home.model.UserProfileUiModel
import com.hilingual.presentation.home.type.DiaryCardState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class HomeUiState(
    val userProfile: UserProfileUiModel = UserProfileUiModel(),
    val hasDiaryTemp: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val diaryThumbnail: DiaryThumbnailUiModel? = null,
    val dateList: ImmutableList<DateUiModel> = persistentListOf(),
    val todayTopic: TodayTopicUiModel? = null,
    val cardState: DiaryCardState = DiaryCardState.PAST
) {
    companion object {
        val Fake = HomeUiState(
            userProfile = UserProfileUiModel(
                nickname = "포도",
                profileImg = "",
                totalDiaries = 12,
                streak = 5,
                isNewAlarm = true
            ),
            selectedDate = LocalDate.now(),
            diaryThumbnail = DiaryThumbnailUiModel(
                diaryId = 1L,
                imageUrl = null,
                originalText = "오늘 날씨가 정말 좋았다. 그래서 산책을 나갔다.",
                isPublished = false
            ),
            dateList = persistentListOf(
                DateUiModel(date = "2025-09-01"),
                DateUiModel(date = "2025-09-05"),
                DateUiModel(date = "2025-09-12"),
                DateUiModel(date = "2025-09-23")
            ),
            todayTopic = TodayTopicUiModel(
                topicKo = "가장 좋아하는 계절은 무엇인가요?",
                topicEn = "What is your favorite season?",
                remainingTime = -1
            )
        )
    }
}
