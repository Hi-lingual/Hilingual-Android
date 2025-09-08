package com.hilingual.presentation.feedprofile.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.repository.FeedRepository
import com.hilingual.presentation.feedprofile.profile.model.DiaryTabType
import com.hilingual.presentation.feedprofile.profile.model.FeedDiaryUIModel
import com.hilingual.presentation.feedprofile.profile.model.FeedProfileInfoModel
import com.hilingual.presentation.feedprofile.profile.model.toState
import com.hilingual.presentation.feedprofile.profile.navigation.FeedProfileGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val targetUserId: Long = savedStateHandle.toRoute<FeedProfileGraph>().userId

    private val _uiState = MutableStateFlow<UiState<FeedProfileUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedProfileUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedProfileSideEffect>()
    val sideEffect: SharedFlow<FeedProfileSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadFeedProfile()
        loadLikedDiaries()
    }

    private fun loadFeedProfile() {
        viewModelScope.launch {
            val feedProfileDeferred = async { feedRepository.getFeedProfile(targetUserId) }
            val sharedDiariesDeferred = async { feedRepository.getSharedDiaries(targetUserId) }

            val feedProfileResult = feedProfileDeferred.await()
            val sharedDiariesResult = sharedDiariesDeferred.await()

            if (feedProfileResult.isFailure || sharedDiariesResult.isFailure) {
                feedProfileResult.onLogFailure {}
                sharedDiariesResult.onLogFailure {}
                _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog { loadFeedProfile() })
                return@launch
            }

            val feedProfileInfoModel = feedProfileResult.getOrThrow()
            val sharedDiariesModel = sharedDiariesResult.getOrThrow()

            val sharedDiaryUIModels = sharedDiariesModel.diaryList.map { sharedDiary ->
                sharedDiary.toState(
                    feedProfileInfoModel = feedProfileInfoModel.toState(),
                    authorUserId = targetUserId
                )
            }.toImmutableList()

            _uiState.update {
                UiState.Success(
                    FeedProfileUiState(
                        feedProfileInfo = feedProfileInfoModel.toState(),
                        sharedDiaries = sharedDiaryUIModels
                    )
                )
            }
        }
    }

    private fun loadSharedDiaries(feedProfileIfoModel: FeedProfileInfoModel) {
        viewModelScope.launch {
            feedRepository.getSharedDiaries(targetUserId)
                .onSuccess { sharedDiariesModel ->
                    val sharedDiaryUIModels = sharedDiariesModel.diaryList.map { sharedDiary ->
                        sharedDiary.toState(
                            feedProfileInfoModel = feedProfileIfoModel,
                            authorUserId = targetUserId
                        )
                    }.toImmutableList()

                    _uiState.updateSuccess { currentState ->
                        currentState.copy(sharedDiaries = sharedDiaryUIModels)
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
                        likedDiaryItem.toState()
                    }.toImmutableList()

                    _uiState.updateSuccess { currentState ->
                        currentState.copy(likedDiaries = likedDiaryUIModels)
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog { loadFeedProfile() })
                }
        }
    }

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean, type: DiaryTabType) {
        viewModelScope.launch {
            _uiState.updateSuccess { currentState ->
                when (type) {
                    DiaryTabType.SHARED -> {
                        currentState.copy(
                            sharedDiaries = currentState.sharedDiaries.updateLikeState(diaryId, isLiked)
                        )
                    }

                    DiaryTabType.LIKED -> {
                        currentState.copy(
                            likedDiaries = currentState.likedDiaries.updateLikeState(diaryId, isLiked)
                        )
                    }
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
        viewModelScope.launch {
            _sideEffect.emit(FeedProfileSideEffect.ShowToast(message = "일기가 비공개 되었어요."))
        }
    }
}

sealed interface FeedProfileSideEffect {
    data class ShowToast(val message: String) : FeedProfileSideEffect
    data class ShowRetryDialog(val onRetry: () -> Unit) : FeedProfileSideEffect
}
