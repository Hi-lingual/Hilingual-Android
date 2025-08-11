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
package com.hilingual.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.data.user.model.UserProfileModel
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val specialCharRegex = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<OnboardingSideEffect>()
    val sideEffect: SharedFlow<OnboardingSideEffect> = _sideEffect.asSharedFlow()

    init {
        @OptIn(FlowPreview::class)
        _uiState
            .map { it.nickname }
            .distinctUntilChanged()
            .debounce(700L)
            .onEach(::validateNickname)
            .launchIn(viewModelScope)
    }

    fun onNicknameChanged(newNickname: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nickname = newNickname,
                validationMessage = "",
                isNicknameValid = false
            )
        }
    }

    fun onSubmitNickname(nickname: String) {
        validateNickname(nickname)
    }

    fun onRegisterClick(nickname: String, isMarketingAgreed: Boolean) {
        viewModelScope.launch {
            userRepository.postUserProfile(UserProfileModel(profileImg = "", nickname = nickname))
                .onSuccess {
                    tokenManager.completeOnboarding()
                    _sideEffect.emit(OnboardingSideEffect.NavigateToHome)
                }
                .onLogFailure {
                    _sideEffect.emit(OnboardingSideEffect.ShowRetryDialog { onRegisterClick(nickname, isMarketingAgreed) })
                }
        }
    }

    private fun validateNickname(nickname: String) {
        if (nickname.isBlank()) {
            _uiState.update {
                it.copy(
                    validationMessage = "",
                    isNicknameValid = false
                )
            }
            return
        }
        viewModelScope.launch {
            if (nickname.length < 2) {
                _uiState.update {
                    it.copy(
                        validationMessage = "최소 2글자 이상 입력해주세요",
                        isNicknameValid = false
                    )
                }
                return@launch
            }

            if (specialCharRegex.containsMatchIn(nickname)) {
                _uiState.update {
                    it.copy(
                        validationMessage = "특수문자, 이모지는 사용이 불가능해요",
                        isNicknameValid = false
                    )
                }
                return@launch
            }

            userRepository.getNicknameAvailability(nickname)
                .onSuccess { isAvailable ->
                    if (isAvailable) {
                        _uiState.update {
                            it.copy(
                                validationMessage = "사용 가능한 닉네임이에요",
                                isNicknameValid = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                validationMessage = "이미 사용중인 닉네임이에요",
                                isNicknameValid = false
                            )
                        }
                    }
                }
                .onLogFailure {
                    _uiState.update {
                        it.copy(
                            isNicknameValid = false
                        )
                    }
                    _sideEffect.emit(OnboardingSideEffect.ShowRetryDialog { validateNickname(nickname) })
                }
        }
    }
}

sealed interface OnboardingSideEffect {
    data object NavigateToHome : OnboardingSideEffect
    data class ShowRetryDialog(val onRetry: () -> Unit) : OnboardingSideEffect
}
