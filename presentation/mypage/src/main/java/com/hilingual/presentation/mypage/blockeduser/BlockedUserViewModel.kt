package com.hilingual.presentation.mypage.blockeduser

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class BlockedUserViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BlockedUserUiState())
    val uiState: StateFlow<BlockedUserUiState> = _uiState.asStateFlow()

    fun loadInitialData() {
        val dummy = persistentListOf(
            BlockedUserUiModel(
                userId = 1L,
                profileImageUrl = "",
                nickname = "사과"
            ),
            BlockedUserUiModel(
                userId = 2L,
                profileImageUrl = "",
                nickname = "바나나"
            ),
            BlockedUserUiModel(
                userId = 3L,
                profileImageUrl = "",
                nickname = "오렌지"
            ),
            BlockedUserUiModel(
                userId = 4L,
                profileImageUrl = "https://picsum.photos/42/42?random=1",
                nickname = "딸기"
            ),
            BlockedUserUiModel(
                userId = 5L,
                profileImageUrl = "",
                nickname = "포도"
            )
        )
        _uiState.update { it.copy(blockedUserList = UiState.Success(data = dummy)) }
    }

    fun onUnblockStatusChanged(userId: Long) {
        _uiState.update { currentList ->
            when (val listState = currentList.blockedUserList) {
                is UiState.Success -> {
                    val updatedList = listState.data.map { user ->
                        if (user.userId == userId) user.copy(isBlocked = !user.isBlocked) else user
                    }.toPersistentList()
                    currentList.copy(blockedUserList = UiState.Success(data = updatedList))
                }

                else -> currentList
            }
        }
    }
}
