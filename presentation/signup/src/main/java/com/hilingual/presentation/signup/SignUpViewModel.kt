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
package com.hilingual.presentation.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.ui.model.NicknameValidationStatus
import com.hilingual.core.ui.util.NicknameLocalValidation
import com.hilingual.core.ui.util.NicknameLocalValidationReason
import com.hilingual.core.ui.util.NicknameValidator
import com.hilingual.data.onboarding.repository.OnboardingRepository
import com.hilingual.data.user.model.user.UserProfileModel
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private var isProfileCreated = false

    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> = _sideEffect.asSharedFlow()

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
                validationStatus = NicknameValidationStatus.NONE,
            )
        }
    }

    fun onSubmitNickname(nickname: String) {
        validateNickname(nickname)
    }

    fun onRegisterClick(nickname: String, isMarketingAgreed: Boolean, imageUri: Uri?) {
        if (uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (!isProfileCreated) {
                val userProfile = UserProfileModel(
                    nickname = nickname,
                    adAlarmAgree = isMarketingAgreed,
                    imageUri = imageUri,
                )
                val profileResult = userRepository.postUserProfile(userProfile)
                profileResult
                    .onSuccess { isProfileCreated = true }
                    .onLogFailure { showRegisterRetryDialog(nickname, isMarketingAgreed, imageUri) }
                if (profileResult.isFailure) return@launch
            }
            onProfileRegistered(nickname, isMarketingAgreed, imageUri)
        }
    }

    private fun validateNickname(nickname: String) {
        when (val localValidationResult = NicknameValidator.validateNickname(nickname)) {
            is NicknameLocalValidation.Blank -> {
                _uiState.update {
                    it.copy(validationStatus = NicknameValidationStatus.NONE)
                }
            }

            is NicknameLocalValidation.Invalid -> {
                when (localValidationResult.reason) {
                    NicknameLocalValidationReason.TOO_SHORT -> {
                        _uiState.update {
                            it.copy(validationStatus = NicknameValidationStatus.TOO_SHORT)
                        }
                    }

                    NicknameLocalValidationReason.SPECIAL_CHAR -> {
                        _uiState.update {
                            it.copy(validationStatus = NicknameValidationStatus.SPECIAL_CHAR)
                        }
                    }
                }
            }

            is NicknameLocalValidation.Valid -> {
                viewModelScope.launch {
                    userRepository.getNicknameAvailability(nickname)
                        .onSuccess { result ->
                            _uiState.update {
                                it.copy(validationStatus = NicknameValidationStatus.fromName(result.name))
                            }
                        }
                        .onLogFailure {
                            _uiState.update { it.copy(validationStatus = NicknameValidationStatus.NONE) }
                            _sideEffect.emit(SignUpSideEffect.ShowRetryDialog { validateNickname(nickname) })
                        }
                }
            }
        }
    }

    private suspend fun onProfileRegistered(nickname: String, isMarketingAgreed: Boolean, imageUri: Uri?) {
        if (!putDeviceInfo()) {
            showRegisterRetryDialog(nickname, isMarketingAgreed, imageUri)
            return
        }
        userRepository.saveRegisterStatus(true)
        onboardingRepository.updateIsHomeOnboardingCompleted(false)
        updateIsSplashOnboardingCompleted()
        _sideEffect.emit(SignUpSideEffect.NavigateToHome)
    }

    private suspend fun putDeviceInfo(): Boolean =
        userRepository.putDeviceInfo().onLogFailure { }.isSuccess

    private suspend fun showRegisterRetryDialog(nickname: String, isMarketingAgreed: Boolean, imageUri: Uri?) {
        _uiState.update { it.copy(isLoading = false) }
        _sideEffect.emit(SignUpSideEffect.ShowRetryDialog { onRegisterClick(nickname, isMarketingAgreed, imageUri) })
    }

    private suspend fun updateIsSplashOnboardingCompleted() {
        onboardingRepository.completeSplashOnboarding()
            .onLogFailure { }
    }
}

sealed interface SignUpSideEffect {
    data object NavigateToHome : SignUpSideEffect
    data class ShowRetryDialog(val onRetry: () -> Unit) : SignUpSideEffect
}
