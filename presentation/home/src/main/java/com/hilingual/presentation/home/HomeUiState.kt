package com.hilingual.presentation.home

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.DiaryThumbnailUiModel
import com.hilingual.presentation.home.model.TodayTopicUiModel
import com.hilingual.presentation.home.model.UserProfileUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class HomeUiState(
    val userProfile: UserProfileUiModel = UserProfileUiModel(),
    val selectedDate: LocalDate = LocalDate.now(),
    val diaryThumbnail: DiaryThumbnailUiModel? = null,
    val dateList: ImmutableList<DateUiModel> = persistentListOf(),
    val isDiaryThumbnailLoading: Boolean = false,
    val todayTopic: TodayTopicUiModel? = null,
)
