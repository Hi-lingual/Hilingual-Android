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
import com.hilingual.core.common.analytics.UserIdentityTracker
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.ui.model.user.NicknameLocalValidation
import com.hilingual.core.ui.model.user.NicknameLocalValidationReason
import com.hilingual.core.ui.model.user.NicknameValidationStatus
import com.hilingual.core.ui.util.NicknameValidator
import com.hilingual.data.onboarding.repository.OnboardingRepository
import com.hilingual.data.user.model.user.UserProfileModel
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
    private val userIdentityTracker: UserIdentityTracker,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private var isProfileCreated = false

    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> = _sideEffect.asSharedFlow()

    private var nicknameValidationDebounceJob: Job? = null
    private var nicknameValidationJob: Job? = null

    fun onNicknameChanged(newNickname: String) {
        cancelNicknameValidation()

        _uiState.update { currentState ->
            currentState.copy(
                nickname = newNickname,
                validationStatus = NicknameValidationStatus.NONE,
            )
        }

        nicknameValidationDebounceJob = viewModelScope.launch {
            delay(700L)
            runNicknameValidation(newNickname)
        }
    }

    fun onSubmitNickname(nickname: String) {
        nicknameValidationDebounceJob?.cancel()
        runNicknameValidation(nickname)
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
                    .onSuccess { userId ->
                        isProfileCreated = true
                        userIdentityTracker.setUserId(userId)
                    }
                    .onLogFailure { showRegisterRetryDialog(nickname, isMarketingAgreed, imageUri) }
                if (profileResult.isFailure) return@launch
            }
            onProfileRegistered(nickname, isMarketingAgreed, imageUri)
        }
    }

    private fun runNicknameValidation(nickname: String) {
        nicknameValidationJob?.cancel()
        nicknameValidationJob = viewModelScope.launch {
            validateNickname(nickname)
        }
    }

    private fun cancelNicknameValidation() {
        nicknameValidationDebounceJob?.cancel()
        nicknameValidationJob?.cancel()
    }

    private suspend fun validateNickname(nickname: String) {
        when (val localValidationResult = NicknameValidator.validateNickname(nickname)) {
            is NicknameLocalValidation.Blank -> {
                updateNicknameValidationStatus(nickname, NicknameValidationStatus.NONE)
            }

            is NicknameLocalValidation.Invalid -> {
                updateInvalidNicknameValidationStatus(nickname, localValidationResult.reason)
            }

            is NicknameLocalValidation.Valid -> {
                updateAvailableNicknameValidationStatus(nickname)
            }
        }
    }

    private fun updateInvalidNicknameValidationStatus(
        nickname: String,
        reason: NicknameLocalValidationReason,
    ) {
        val validationStatus = when (reason) {
            NicknameLocalValidationReason.TOO_SHORT -> NicknameValidationStatus.TOO_SHORT
            NicknameLocalValidationReason.SPECIAL_CHAR -> NicknameValidationStatus.SPECIAL_CHAR
        }
        updateNicknameValidationStatus(nickname, validationStatus)
    }

    private suspend fun updateAvailableNicknameValidationStatus(nickname: String) {
        userRepository.getNicknameAvailability(nickname)
            .onSuccess { result ->
                updateNicknameValidationStatus(nickname, NicknameValidationStatus.fromName(result.name))
            }
            .onLogFailure {
                updateNicknameValidationStatus(nickname, NicknameValidationStatus.NONE)
                _sideEffect.emit(SignUpSideEffect.ShowRetryDialog { runNicknameValidation(nickname) })
            }
    }

    private fun updateNicknameValidationStatus(
        nickname: String,
        validationStatus: NicknameValidationStatus,
    ) {
        _uiState.update {
            if (it.nickname == nickname) {
                it.copy(validationStatus = validationStatus)
            } else {
                it
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
        viewModelScope.launch { userRepository.syncFcmToken() }
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
