package com.hilingual.presentation.feedprofile.profile

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feedprofile.profile.model.FeedProfileInfoModel
import com.hilingual.presentation.feedprofile.profile.model.SharedDiaryItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class FeedProfileViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FeedProfileUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedProfileUiState>> = _uiState.asStateFlow()

    init {
        _uiState.value = UiState.Success(
            FeedProfileUiState(
                feedProfileInfo = FeedProfileInfoModel(
                    profileImageUrl = "",
                    nickname = "가짜프로필",
                    streak = 3,
                    follower = 10,
                    following = 5,
                    isMine = true,
                    isFollowing = false,
                    isFollowed = false,
                    isBlock = false
                ),
                sharedDiarys = kotlinx.collections.immutable.persistentListOf(
                    SharedDiaryItemModel(
                        profileImageUrl = "",
                        nickname = "가짜프로필",
                        diaryId = 1L,
                        sharedDate = 1720000000L,
                        likeCount = 12,
                        isLiked = false,
                        diaryImageUrl = null,
                        originalText = "가짜 사용자의 일기 내용입니다."
                    )
                ),
                likedDiarys = kotlinx.collections.immutable.persistentListOf()
            )
        )
    }
}
