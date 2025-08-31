package com.hilingual.presentation.feedprofile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feedprofile.profile.model.LikeDiaryItemModel
import com.hilingual.presentation.feedprofile.profile.model.SharedDiaryItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
internal class FeedProfileViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FeedProfileUiState>>(UiState.Success(FeedProfileUiState.Fake))
    val uiState: StateFlow<UiState<FeedProfileUiState>> = _uiState.asStateFlow()
    private val _sideEffect = MutableSharedFlow<FeedProfileSideEffect>()
    val sideEffect: SharedFlow<FeedProfileSideEffect> = _sideEffect.asSharedFlow()

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val successState = currentState as UiState.Success<FeedProfileUiState>

                fun updateSharedDiary(item: SharedDiaryItemModel): SharedDiaryItemModel {
                    return if (item.diaryId == diaryId) {
                        item.copy(
                            isLiked = isLiked,
                            likeCount = item.likeCount + if (isLiked) 1 else -1
                        )
                    } else {
                        item
                    }
                }

                fun updateLikedDiary(item: LikeDiaryItemModel): LikeDiaryItemModel {
                    return if (item.diaryId == diaryId) {
                        item.copy(
                            isLiked = isLiked,
                            likeCount = item.likeCount + if (isLiked) 1 else -1
                        )
                    } else {
                        item
                    }
                }

                successState.copy(
                    data = successState.data.copy(
                        sharedDiaries = successState.data.sharedDiaries
                            .map(::updateSharedDiary)
                            .toImmutableList(),
                        likedDiaries = successState.data.likedDiaries
                            .map(::updateLikedDiary)
                            .toImmutableList()
                    )
                )
            }
        }
    }

    fun diaryUnpublish(diaryId: Long) {
        viewModelScope.launch {
            _sideEffect.emit(FeedProfileSideEffect.ShowToast(message = "일기가 비공개 되었어요."))
        }
    }
}

sealed interface FeedProfileSideEffect {
    data class ShowToast(val message: String) : FeedProfileSideEffect
}
