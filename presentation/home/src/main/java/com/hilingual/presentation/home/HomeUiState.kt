package com.hilingual.presentation.home

import androidx.compose.runtime.Immutable
import com.hilingual.data.user.model.UserInfoModel
import java.time.LocalDate
import com.hilingual.data.calendar.model.DateModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HomeUiState(
    val userProfile: UserProfile = UserProfile(),
    val selectedDate: LocalDate = LocalDate.now(),
    val writtenDates: Set<LocalDate> = emptySet(),
    val diaryPreview: DiaryPreview? = null,
    val todayTopic: TodayTopic? = null,
    val isLoading: Boolean = true,
    val dateList: ImmutableList<DateUiModel> = persistentListOf()
)

@Immutable
data class UserProfile(
    val nickname: String = "",
    val profileImg: String = "",
    val totalDiaries: Int = 0,
    val streak: Int = 0
)

fun UserInfoModel.toState() = UserProfile(
    nickname = this.nickname,
    profileImg = this.profileImg,
    totalDiaries = this.totalDiaries,
    streak = this.streak
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

@Immutable
data class DateUiModel(
    val date: String
)

fun DateModel.toUiModel() = DateUiModel(
    date = this.date
)
