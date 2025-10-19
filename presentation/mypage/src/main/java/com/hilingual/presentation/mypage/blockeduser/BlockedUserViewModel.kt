/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.mypage.blockeduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
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
internal class BlockedUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BlockedUserUiState())
    val uiState: StateFlow<BlockedUserUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<BlockedUserSideEffect>()
    val sideEffect: SharedFlow<BlockedUserSideEffect> = _sideEffect.asSharedFlow()

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
                    }.toImmutableList()

                    _uiState.update { it.copy(blockedUserList = UiState.Success(data = blockUiModel)) }
                }
                .onLogFailure {
                    viewModelScope.launch {
                        _sideEffect.emit(BlockedUserSideEffect.ShowErrorDialog)
                    }
                }
        }
    }

    fun onUnblockStatusChanged(userId: Long) {
        viewModelScope.launch {
            val blockedUserListState = _uiState.value.blockedUserList
            if (blockedUserListState !is UiState.Success) return@launch

            val currentList = blockedUserListState.data
            val targetUser = currentList.find { it.userId == userId } ?: return@launch

            val apiResult = if (targetUser.isBlocked) {
                userRepository.deleteBlockUser(targetUserId = userId)
            } else {
                userRepository.putBlockUser(targetUserId = userId)
            }

            apiResult
                .onSuccess {
                    _uiState.update { state ->
                        val successState = state.blockedUserList as? UiState.Success
                        if (successState != null) {
                            val updatedList = successState.data.map { user ->
                                if (user.userId == userId) user.copy(isBlocked = !user.isBlocked) else user
                            }.toImmutableList()

                            state.copy(blockedUserList = UiState.Success(data = updatedList))
                        } else {
                            state
                        }
                    }
                }
                .onLogFailure {
                    viewModelScope.launch {
                        _sideEffect.emit(BlockedUserSideEffect.ShowErrorDialog)
                    }
                }
        }
    }
}

sealed interface BlockedUserSideEffect {
    data object ShowErrorDialog : BlockedUserSideEffect
}
