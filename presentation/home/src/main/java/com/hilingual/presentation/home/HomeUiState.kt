package com.hilingual.presentation.home

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class HomeUiState(
    val userProfile: UserProfile = UserProfile(),
    val selectedDate: LocalDate = LocalDate.now(),
    val writtenDates: Set<LocalDate> = emptySet(),
    val diaryPreview: DiaryPreview? = null,
    val todayTopic: TodayTopic? = null,
    val isLoading: Boolean = true
)

@Immutable
data class UserProfile(
    val nickname: String = "",
    val profileImg: String = "",
    val totalDiaries: Int = 0,
    val streak: Int = 0
)

@Immutable
data class DiaryPreview(
    val diaryId: Long,
    val createdAt: String,
    val imageUrl: String? = "https://avatars.githubusercontent.com/u/160750136?v=4",
    val originalText: String
)

@Immutable
data class TodayTopic(
    val topicKo: String,
    val topicEn: String,
    val remainingTime: Int
)