package com.hilingual.presentation.mypage

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
internal class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MyPageUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<MyPageUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MyPageSideEffect>()
    val sideEffect: SharedFlow<MyPageSideEffect> = _sideEffect.asSharedFlow()

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
                                profileProvider = userInfo.provider
                            )
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    fun patchProfileImage(newImageUri: Uri?) {
        // TODO: 프로필 사진 변경 PATCH API 연결 by 지영
        val current = (_uiState.value as? UiState.Success)?.data ?: return

        _uiState.value = UiState.Success(
            current.copy(profileImageUrl = newImageUri?.toString().orEmpty())
        )
    }

    fun logout() {
        // TODO: 로그아웃 POST API 연결 by 지영
        viewModelScope.launch {
            _sideEffect.emit(MyPageSideEffect.RestartApp)
        }
    }

    fun withdraw() {
        // TODO: 회원탈퇴 DELETE API 연결 by 지영
        viewModelScope.launch {
            _sideEffect.emit(MyPageSideEffect.RestartApp)
        }
    }
}

sealed interface MyPageSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : MyPageSideEffect
    data object RestartApp : MyPageSideEffect
}
