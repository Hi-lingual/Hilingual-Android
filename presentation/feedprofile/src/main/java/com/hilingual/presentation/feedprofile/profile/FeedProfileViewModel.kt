package com.hilingual.presentation.feedprofile.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.repository.FeedRepository
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

// TODO: 아래의 UiState는 현재 Fake 데이터로 초기화되어 있습니다.
//  실제 데이터 연동 시, ViewModel은 Repository로부터 `SharedDiaryItemModel` 또는 `LikeDiaryItemModel`을
//  가져온 뒤, 아래와 같은 로직으로 UI 전용 모델인 `FeedDiaryUIModel`로 변환해야 합니다.
//
//  - SharedDiaryItemModel -> FeedDiaryUIModel:
//    - `isMine` = true
//    - `authorUserId`, `authorNickname`, `authorProfileImageUrl`, `authorStreak` 등은
//      프로필 주인의 정보(`feedProfileInfo`)로 채워줍니다.
//
//  - LikeDiaryItemModel -> FeedDiaryUIModel:
//    - `isMine`, `userId`, `nickname`, `streak` 등 필요한 모든 정보가 이미 모델 안에 있으므로
//      해당 값을 그대로 사용하여 `FeedDiaryUIModel`을 생성합니다.
@HiltViewModel
internal class FeedProfileViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val targetUserId: Long = savedStateHandle.get<Long>("targetUserId") ?: 0L
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
                }
                .onLogFailure {
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog {})
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
                    _sideEffect.emit(FeedProfileSideEffect.ShowRetryDialog {})
                }
        }
    }

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean, type: DiaryTabType) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val successState = currentState as? UiState.Success ?: return@update currentState

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
