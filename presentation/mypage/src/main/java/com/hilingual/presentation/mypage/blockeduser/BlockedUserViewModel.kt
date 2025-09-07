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
        viewModelScope.launch {
            val currentList = (_uiState.value.blockedUserList as? UiState.Success)?.data
            val targetUser = currentList?.find { it.userId == userId }

            if (currentList != null && targetUser != null) {
                val apiFunction = if (targetUser.isBlocked) {
                    userRepository.deleteBlockUser(targetUserId = userId)
                } else {
                    userRepository.putBlockUser(targetUserId = userId)
                }

                apiFunction
                    .onSuccess {
                        _uiState.update { state ->
                            val successState = state.blockedUserList as? UiState.Success
                            if (successState != null) {
                                val updatedList = successState.data.map { user ->
                                    if (user.userId == userId) user.copy(isBlocked = !user.isBlocked) else user
                                }.toPersistentList()

                                state.copy(blockedUserList = UiState.Success(data = updatedList))
                            } else {
                                state
                            }
                        }
                    }
                    .onLogFailure { }
            }
        }
    }
}
