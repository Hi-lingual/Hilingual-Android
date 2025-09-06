package com.hilingual.presentation.mypage.blockeduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BlockedUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BlockedUserUiState())
    val uiState: StateFlow<BlockedUserUiState> = _uiState.asStateFlow()

    fun getBlockList() {
        viewModelScope.launch {
            userRepository.getBlockList()
                .onSuccess { blockList ->
                    val blockUiModel = blockList.blockList.map { user ->
                        BlockedUserUiModel(
                            userId = user.userId,
                            profileImageUrl = user.profileImageUrl,
                            nickname = user.nickname
                        )
                    }.toPersistentList()

                    _uiState.update { it.copy(blockedUserList = UiState.Success(data = blockUiModel)) }
                }
                .onLogFailure { }
        }
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
