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
import com.hilingual.presentation.home.type.NotificationPermissionState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class HomeUiState(
    val header: HomeHeaderUiState = HomeHeaderUiState(),
    val calendar: HomeCalendarUiState = HomeCalendarUiState(),
    val diaryContent: HomeDiaryUiState = HomeDiaryUiState()
) {
    companion object {
        val Fake = HomeUiState(
            header = HomeHeaderUiState(
                userProfile = UserProfileUiModel(
                    nickname = "포도",
                    profileImg = "",
                    totalDiaries = 12,
                    streak = 5,
                    isNewAlarm = true
                )
            ),
            calendar = HomeCalendarUiState(
                selectedDate = LocalDate.now(),
                dates = persistentListOf(
                    DateUiModel(date = LocalDate.of(2025, 9, 1)),
                    DateUiModel(date = LocalDate.of(2025, 9, 5)),
                    DateUiModel(date = LocalDate.of(2025, 9, 12)),
                    DateUiModel(date = LocalDate.of(2025, 9, 23))
                )
            ),
            diaryContent = HomeDiaryUiState(
                diaryThumbnail = DiaryThumbnailUiModel(
                    diaryId = 1L,
                    imageUrl = null,
                    originalText = "오늘 날씨가 정말 좋았다. 그래서 산책을 나갔다.",
                    isPublished = false
                ),
                todayTopic = TodayTopicUiModel(
                    topicKo = "가장 좋아하는 계절은 무엇인가요?",
                    topicEn = "What is your favorite season?",
                    remainingTime = -1
                ),
                cardState = DiaryCardState.PAST
            )
        )
    }
}

@Immutable
data class HomeHeaderUiState(
    val userProfile: UserProfileUiModel = UserProfileUiModel(),
    val notificationPermissionState: NotificationPermissionState = NotificationPermissionState.NOT_DETERMINED
)

@Immutable
data class HomeCalendarUiState(
    val dates: ImmutableList<DateUiModel> = persistentListOf(),
    val selectedDate: LocalDate = LocalDate.now()
) {
    fun selectDate(date: LocalDate): HomeCalendarUiState {
        return copy(selectedDate = date)
    }
}

@Immutable
data class HomeDiaryUiState(
    val isDiaryTempExist: Boolean = false,
    val diaryThumbnail: DiaryThumbnailUiModel? = null,
    val todayTopic: TodayTopicUiModel? = null,
    val cardState: DiaryCardState = DiaryCardState.PAST
) {
    fun update(
        selectedDate: LocalDate,
        dates: List<DateUiModel>,
        fetchedThumbnail: DiaryThumbnailUiModel? = null,
        fetchedTopic: TodayTopicUiModel? = null,
        isTempExist: Boolean = this.isDiaryTempExist
    ): HomeDiaryUiState {
        // Use Rich Model Logic
        val selectedDateModel = DateUiModel(selectedDate)

        if (selectedDateModel.isFuture) {
            return copy(
                cardState = DiaryCardState.FUTURE,
                diaryThumbnail = null,
                todayTopic = null,
                isDiaryTempExist = isTempExist
            )
        }

        val isWritten = dates.any { it.date == selectedDate }
        if (isWritten) {
            return copy(
                cardState = DiaryCardState.WRITTEN,
                diaryThumbnail = fetchedThumbnail,
                todayTopic = null,
                isDiaryTempExist = isTempExist
            )
        }

        if (selectedDateModel.isWritable) {
            val topicState = if (fetchedTopic?.remainingTime == -1) {
                DiaryCardState.REWRITE_DISABLED
            } else {
                DiaryCardState.WRITABLE
            }
            
            return copy(
                cardState = topicState,
                diaryThumbnail = null,
                todayTopic = fetchedTopic,
                isDiaryTempExist = isTempExist
            )
        }

        return copy(
            cardState = DiaryCardState.PAST,
            diaryThumbnail = null,
            todayTopic = null,
            isDiaryTempExist = isTempExist
        )
    }
}