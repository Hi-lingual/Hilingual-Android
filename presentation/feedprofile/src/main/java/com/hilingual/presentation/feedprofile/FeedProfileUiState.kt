package com.hilingual.presentation.feedprofile

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feedprofile.model.FeedProfileInfoModel
import com.hilingual.presentation.feedprofile.model.LikeDiaryItemModel
import com.hilingual.presentation.feedprofile.model.SharedDiaryItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FeedProfileUiState(
    val feedProfileInfo: UiState<FeedProfileInfoModel> = UiState.Loading,
    val sharedDiarys: ImmutableList<SharedDiaryItemModel> = persistentListOf(),
    val likedDiarys: ImmutableList<LikeDiaryItemModel> = persistentListOf()
)
