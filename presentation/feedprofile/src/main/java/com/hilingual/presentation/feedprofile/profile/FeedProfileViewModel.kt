package com.hilingual.presentation.feedprofile.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
//import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.model.IsLikedModel
import com.hilingual.data.feed.repository.FeedRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feedprofile.profile.model.DiaryTabType
import com.hilingual.presentation.feedprofile.profile.model.FeedDiaryUIModel
import com.hilingual.presentation.feedprofile.profile.model.toFeedDiaryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FeedProfileViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
    //private val diaryRepository: DiaryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val targetUserId: Long = savedStateHandle.get<Long>("userId") ?: 0L
    private val _uiState = MutableStateFlow<UiState<FeedProfileUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedProfileUiState>> = _uiState.asStateFlow()
    private val _sideEffect = MutableSharedFlow<FeedProfileSideEffect>()
    val sideEffect: SharedFlow<FeedProfileSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadFeedProfile()
    }

    private fun loadFeedProfile() {
        viewModelScope.launch {
            feedRepository.getFeedProfile(targetUserId)
                .onSuccess { feedProfileModel ->
                    _uiState.update {
                        UiState.Success(
                            FeedProfileUiState(
                                feedProfileInfo = feedProfileModel,
                                sharedDiaries = persistentListOf(),
                                likedDiaries = persistentListOf()
                            )
                        )
                    }
                    loadSharedDiaries(feedProfileModel)
                    loadLikedDiaries()
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog { loadFeedProfile() })
                }
        }
    }

    private fun loadSharedDiaries(feedProfileModel: FeedProfileModel) {
        viewModelScope.launch {
            feedRepository.getSharedDiaries(targetUserId)
                .onSuccess { sharedDiariesModel ->
                    val sharedDiaryUIModels = sharedDiariesModel.diaryList.map { sharedDiary ->
                        sharedDiary.toFeedDiaryUIModel(
                            feedProfileModel = feedProfileModel,
                            authorUserId = targetUserId
                        )
                    }.toImmutableList()

                    _uiState.update { currentState ->
                        val successState = currentState as? UiState.Success ?: return@update currentState
                        successState.copy(
                            data = successState.data.copy(sharedDiaries = sharedDiaryUIModels)
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog { loadFeedProfile() })
                }
        }
    }

    private fun loadLikedDiaries() {
        viewModelScope.launch {
            feedRepository.getLikedDiaries(targetUserId)
                .onSuccess { likedDiariesModel ->
                    val likedDiaryUIModels = likedDiariesModel.diaryList.map { likedDiaryItem ->
                        likedDiaryItem.toFeedDiaryUIModel()
                    }.toImmutableList()

                    _uiState.update { currentState ->
                        val successState = currentState as? UiState.Success ?: return@update currentState
                        successState.copy(
                            data = successState.data.copy(likedDiaries = likedDiaryUIModels)
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog { loadFeedProfile() })
                }
        }
    }

    fun refreshProfile() {
        viewModelScope.launch {
            feedRepository.getFeedProfile(targetUserId)
                .onSuccess { feedProfileModel ->
                    _uiState.update { currentState ->
                        val successState = currentState as? UiState.Success ?: return@update currentState
                        successState.copy(
                            data = successState.data.copy(feedProfileInfo = feedProfileModel)
                        )
                    }
                }
                .onLogFailure {
                }
        }
    }

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean, type: DiaryTabType) {
        viewModelScope.launch {
            feedRepository.postIsLiked(
                diaryId = diaryId,
                isLikedModel = IsLikedModel(isLiked)
            )
                .onSuccess {
                    _uiState.update { currentState ->
                        val successState =
                            currentState as? UiState.Success ?: return@update currentState

                        val updatedData = when (type) {
                            DiaryTabType.SHARED -> {
                                successState.data.copy(
                                    sharedDiaries = successState.data.sharedDiaries.updateLikeState(diaryId, isLiked)
                                )
                            }

                            DiaryTabType.LIKED -> {
                                successState.data.copy(
                                    likedDiaries = successState.data.likedDiaries.updateLikeState(diaryId, isLiked)
                                )
                            }
                        }
                        successState.copy(data = updatedData)
                    }
                }
        }
    }

    private fun ImmutableList<FeedDiaryUIModel>.updateLikeState(diaryId: Long, isLiked: Boolean): ImmutableList<FeedDiaryUIModel> {
        val targetIndex = this.indexOfFirst { it.diaryId == diaryId }
        if (targetIndex == -1) return this

        val targetItem = this[targetIndex]
        val updatedItem = targetItem.copy(
            isLiked = isLiked,
            likeCount = targetItem.likeCount + if (isLiked) 1 else -1
        )

        return this.toMutableList().apply {
            set(targetIndex, updatedItem)
        }.toImmutableList()
    }

    fun diaryUnpublish(diaryId: Long) {
        //TODO: 작나 pr 머지 되고 주석 삭제 처리
        /*viewModelScope.launch {
            diaryRepository.patchDiaryUnpublish(diaryId)
                .onSuccess {
                    _sideEffect.emit(FeedProfileSideEffect.ShowToast(message = "일기가 비공개 되었어요."))
                    _uiState.update { currentState ->
                        val successState =
                            currentState as? UiState.Success ?: return@update currentState

                        val updatedSharedDiaries = successState.data.sharedDiaries
                            .filter { it.diaryId != diaryId }
                            .toImmutableList()

                        successState.copy(
                            data = successState.data.copy(sharedDiaries = updatedSharedDiaries)
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowToast("일기 비공개에 실패했습니다."))
                }
        }*/
    }

    fun updateFollowingState(isCurrentlyFollowing: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyFollowing) {
                userRepository.deleteFollow(targetUserId)
            } else {
                userRepository.putFollow(targetUserId)
            }

            result.onSuccess {
                _uiState.update { currentState ->
                    val successState = currentState as? UiState.Success ?: return@update currentState
                    val currentProfile = successState.data.feedProfileInfo

                    val updatedProfile = currentProfile.copy(
                        isFollowing = !isCurrentlyFollowing,
                        follower = if (isCurrentlyFollowing) currentProfile.follower - 1 else currentProfile.follower + 1
                    )

                    successState.copy(
                        data = successState.data.copy(feedProfileInfo = updatedProfile)
                    )
                }
            }.onFailure {
                _sideEffect.emit(FeedProfileSideEffect.ShowToast("팔로우 상태 변경에 실패했습니다."))
            }
        }
    }

    fun refreshTab(tabType: DiaryTabType) {
        val currentState = _uiState.value as? UiState.Success ?: return
        val feedProfileModel = currentState.data.feedProfileInfo

        when (tabType) {
            DiaryTabType.SHARED -> loadSharedDiaries(feedProfileModel)
            DiaryTabType.LIKED -> loadLikedDiaries()
        }
    }

}

sealed interface FeedProfileSideEffect {
    data class ShowToast(val message: String) : FeedProfileSideEffect
    data class ShowRetryDialog(val onRetry: () -> Unit) : FeedProfileSideEffect
}