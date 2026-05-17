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
import com.hilingual.core.ui.model.NicknameValidationStatus
import com.hilingual.core.ui.util.NicknameLocalValidation
import com.hilingual.core.ui.util.NicknameLocalValidationReason
import com.hilingual.core.ui.util.NicknameValidator
import com.hilingual.data.auth.repository.AuthRepository
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

    init {
        getProfileInfo()
        observeNicknameInput()
    }

    @OptIn(FlowPreview::class)
    private fun observeNicknameInput() {
        _nicknameEditState
            .map { it.nickname }
            .distinctUntilChanged()
            .debounce(700L)
            .onEach(::validateNickname)
            .launchIn(viewModelScope)
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
        val currentNickname = (_uiState.value as? UiState.Success)?.data?.profileNickname ?: ""
        _nicknameEditState.update {
            NicknameEditState(nickname = currentNickname)
        }
    }

    fun onNicknameInputChanged(newNickname: String) {
        _nicknameEditState.update {
            it.copy(
                nickname = newNickname,
                validationStatus = NicknameValidationStatus.NONE,
            )
        }
    }

    fun onSubmitNickname(nickname: String) {
        validateNickname(nickname)
    }

    fun onConfirmNicknameChange() {
        val nickname = _nicknameEditState.value.nickname
        viewModelScope.launch {
            userRepository.updateNickname(nickname)
                .onSuccess {
                    getProfileInfo()
                    _sideEffect.emit(MyPageSideEffect.NavigateToProfileEdit)
                }
                .onLogFailure {
                    _sideEffect.emit(MyPageSideEffect.ShowToast("닉네임 업데이트에 실패했어요."))
                }
        }
    }

    private fun validateNickname(nickname: String) {
        val currentNickname = (_uiState.value as? UiState.Success)?.data?.profileNickname
        if (nickname == currentNickname) {
            _nicknameEditState.update {
                it.copy(validationStatus = NicknameValidationStatus.NONE)
            }
            return
        }

        when (val localValidationResult = NicknameValidator.validateNickname(nickname)) {
            is NicknameLocalValidation.Blank -> {
                _nicknameEditState.update {
                    it.copy(validationStatus = NicknameValidationStatus.NONE)
                }
            }

            is NicknameLocalValidation.Invalid -> {
                when (localValidationResult.reason) {
                    NicknameLocalValidationReason.TOO_SHORT -> {
                        _nicknameEditState.update {
                            it.copy(validationStatus = NicknameValidationStatus.TOO_SHORT)
                        }
                    }

                    NicknameLocalValidationReason.SPECIAL_CHAR -> {
                        _nicknameEditState.update {
                            it.copy(validationStatus = NicknameValidationStatus.SPECIAL_CHAR)
                        }
                    }
                }
            }

            is NicknameLocalValidation.Valid -> {
                viewModelScope.launch {
                    userRepository.getNicknameAvailability(nickname)
                        .onSuccess { result ->
                            _nicknameEditState.update {
                                it.copy(validationStatus = result.toValidationStatus())
                            }
                        }
                        .onLogFailure {
                            _nicknameEditState.update {
                                it.copy(validationStatus = NicknameValidationStatus.NONE)
                            }
                        }
                }
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
