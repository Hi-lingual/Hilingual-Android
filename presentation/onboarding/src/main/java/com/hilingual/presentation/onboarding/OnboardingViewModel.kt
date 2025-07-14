package com.hilingual.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val specialCharRegex = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        @OptIn(FlowPreview::class)
        _uiState
            .debounce(1000L)
            .onEach { uiState ->
                if (uiState.validationMessage.isEmpty() && !uiState.isNicknameValid) {
                    validateNickname(uiState.nickname)
                }
            }
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

    fun onDoneAction() {
        validateNickname(_uiState.value.nickname)
    }

    private fun validateNickname(nickname: String) {
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
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isNicknameValid = false
                        )
                    }
                }
        }
    }
}
