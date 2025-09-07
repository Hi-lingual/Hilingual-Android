package com.hilingual.presentation.feed.search

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.model.FollowState
import com.hilingual.presentation.feed.model.UserSearchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
internal class FeedSearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FeedSearchUiState())
    val uiState: StateFlow<FeedSearchUiState> = _uiState.asStateFlow()

    init {
        // 검색 화면 -> 피드 프로필 이동 테스트용
        _uiState.value = _uiState.value.copy(
            searchResultUserList = UiState.Success(
                dummyUsers.toImmutableList()
            )
        )
    }

    fun updateSearchWord(searchWord: String) {
        _uiState.update { it.copy(searchWord = searchWord) }
    }

    fun searchUser() {
        // TODO: API 호출
        _uiState.value = _uiState.value.copy(
            searchResultUserList = UiState.Success(
                tempSearch(_uiState.value.searchWord).toImmutableList()
            )
        )
    }

    fun clearSearchWord() {
        _uiState.update {
            it.copy(searchWord = "")
        }
    }

    fun updateFollowingState(userId: Long, currentIsFollowing: Boolean) {
        // TODO: currentIsFollowing에 따라 취소/등록 API 호출
        _uiState.update { currentState ->
            val oldState = currentState.searchResultUserList

            if (oldState is UiState.Success) {
                val updatedList = oldState.data.map { user ->
                    if (user.userId == userId) {
                        val newState = FollowState.getValueByFollowState(
                            isFollowing = !currentIsFollowing,
                            isFollowed = user.followState.isFollowed
                        )
                        user.copy(followState = newState)
                    } else {
                        user
                    }
                }.toImmutableList()

                currentState.copy(
                    searchResultUserList = UiState.Success(updatedList)
                )
            } else {
                currentState
            }
        }
    }

    companion object {
        val dummyUsers = persistentListOf(
            UserSearchUiModel(
                userId = 1L,
                nickname = "작나",
                profileUrl = "",
                followState = FollowState.ONLY_FOLLOWING
            ),
            UserSearchUiModel(
                userId = 2L,
                nickname = "큰나",
                profileUrl = "",
                followState = FollowState.MUTUAL_FOLLOW
            ),
            UserSearchUiModel(
                userId = 3L,
                nickname = "Daljyeong",
                profileUrl = "",
                followState = FollowState.NONE
            ),
            UserSearchUiModel(
                userId = 4L,
                nickname = "Makers",
                profileUrl = "",
                followState = FollowState.ONLY_FOLLOWED
            )
        )

        fun tempSearch(searchKeyword: String) = dummyUsers.filter {
            it.nickname.contains(searchKeyword)
        }
    }
}
