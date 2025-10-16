package com.hilingual.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.auth.repository.AuthRepository
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<OtpSideEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sideEffect = _sideEffect.asSharedFlow()

    fun updateCode(code: String) {
        if (code.length <= 6) {
            _uiState.update { it.copy(code = code, errorMessage = "") }
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentCode = _uiState.value.code
            if (currentCode == BuildConfig.ADMIN_OTP_CODE) {
                _sideEffect.tryEmit(OtpSideEffect.NavigateToOnboarding)
                return@launch
            }

            authRepository.verifyCode(currentCode)
                .onSuccess {
                    userRepository.saveOtpVerified(true)
                    _sideEffect.tryEmit(OtpSideEffect.NavigateToOnboarding)
                }.onLogFailure {
                    _uiState.update {
                        val newFailureCount = it.failureCount + 1
                        it.copy(
                            failureCount = newFailureCount,
                            errorMessage = "유효하지 않은 인증코드입니다. [실패 횟수 $newFailureCount/5]"
                        )
                    }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

sealed interface OtpSideEffect {
    data object NavigateToOnboarding : OtpSideEffect
}
