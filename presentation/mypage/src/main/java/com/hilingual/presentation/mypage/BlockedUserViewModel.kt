package com.hilingual.presentation.mypage

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class BlockedUserViewModel @Inject constructor() : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiState<ImmutableList<BlockedUserUiState>>>(UiState.Loading)
    val uiState: StateFlow<UiState<ImmutableList<BlockedUserUiState>>> = _uiState.asStateFlow()

    fun loadInitialData() {
        val dummy = persistentListOf(
            BlockedUserUiState(
                userId = 1L,
                profileImageUrl = "",
                nickname = "사과"
            ),
            BlockedUserUiState(
                userId = 2L,
                profileImageUrl = "",
                nickname = "바나나"
            ),
            BlockedUserUiState(
                userId = 3L,
                profileImageUrl = "",
                nickname = "오렌지"
            ),
            BlockedUserUiState(
                userId = 4L,
                profileImageUrl = "https://picsum.photos/42/42?random=1",
                nickname = "딸기"
            ),
            BlockedUserUiState(
                userId = 5L,
                profileImageUrl = "",
                nickname = "포도"
            )
        )
        _uiState.value = UiState.Success(data = dummy)
    }

    fun onUnblockStatusChanged(userId: Long) {
        _uiState.updateSuccess { list ->
            list.map { user ->
                if (user.userId == userId) user.copy(isUnblocked = !user.isUnblocked) else user
            }.toPersistentList()
        }
    }
}
