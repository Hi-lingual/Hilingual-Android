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
package com.hilingual.presentation.mypage

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.app.DeviceInfoProvider
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.core.ui.model.user.NicknameLocalValidation
import com.hilingual.core.ui.model.user.NicknameLocalValidationReason
import com.hilingual.core.ui.model.user.NicknameValidationStatus
import com.hilingual.core.ui.util.NicknameValidator
import com.hilingual.data.auth.repository.AuthRepository
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
internal class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MyPageUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<MyPageUiState>> = _uiState.asStateFlow()

    private val _nicknameEditState = MutableStateFlow(NicknameEditState())
    val nicknameEditState: StateFlow<NicknameEditState> = _nicknameEditState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MyPageSideEffect>()
    val sideEffect: SharedFlow<MyPageSideEffect> = _sideEffect.asSharedFlow()

    private var nicknameValidationDebounceJob: Job? = null
    private var nicknameValidationJob: Job? = null

    init {
        getProfileInfo()
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            userRepository.getUserLoginInfo()
                .onSuccess { userInfo ->
                    _uiState.update {
                        UiState.Success(
                            MyPageUiState(
                                profileImageUrl = userInfo.profileImg,
                                profileNickname = userInfo.nickname,
                                profileProvider = userInfo.provider,
                                appVersion = deviceInfoProvider.getAppVersion(),
                            ),
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowErrorDialog(onRetry = ::getProfileInfo))
                }
        }
    }

    fun patchProfileImage(newImageUri: Uri?) {
        viewModelScope.launch {
            userRepository.updateProfileImage(newImageUri)
                .onSuccess {
                    getProfileInfo()
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowToast("프로필 이미지 업데이트에 실패했어요."))
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
                .onSuccess {
                    _sideEffect.emit(MyPageSideEffect.RestartApp)
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowToast("로그아웃에 실패했어요."))
                }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            authRepository.withdraw()
                .onSuccess {
                    _sideEffect.emit(MyPageSideEffect.RestartApp)
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowToast("회원 탈퇴에 실패했어요."))
                }
        }
    }

    fun initNicknameEdit() {
        cancelNicknameValidation()
        val currentNickname = (_uiState.value as? UiState.Success)?.data?.profileNickname ?: return
        _nicknameEditState.update {
            NicknameEditState(nickname = currentNickname)
        }
    }

    fun onNicknameInputChanged(newNickname: String) {
        cancelNicknameValidation()

        _nicknameEditState.update {
            it.copy(
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

    fun onConfirmNicknameChange() {
        val nicknameEditState = _nicknameEditState.value
        if (!nicknameEditState.isNicknameValid) return

        viewModelScope.launch {
            userRepository.updateNickname(nicknameEditState.nickname)
                .onSuccess {
                    getProfileInfo()
                    _sideEffect.emit(MyPageSideEffect.NavigateToProfileEdit)
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowToast("닉네임 업데이트에 실패했어요."))
                }
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
        val currentNickname = (_uiState.value as? UiState.Success)?.data?.profileNickname
        if (nickname == currentNickname) {
            updateNicknameValidationStatus(nickname, NicknameValidationStatus.NONE)
            return
        }

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
            }
    }

    private fun updateNicknameValidationStatus(
        nickname: String,
        validationStatus: NicknameValidationStatus,
    ) {
        _nicknameEditState.update {
            if (it.nickname == nickname) {
                it.copy(validationStatus = validationStatus)
            } else {
                it
            }
        }
    }
}

sealed interface MyPageSideEffect {
    data class ShowErrorDialog(val onRetry: () -> Unit) : MyPageSideEffect
    data class ShowToast(val message: String) : MyPageSideEffect
    data object RestartApp : MyPageSideEffect
    data object NavigateToProfileEdit : MyPageSideEffect
}
