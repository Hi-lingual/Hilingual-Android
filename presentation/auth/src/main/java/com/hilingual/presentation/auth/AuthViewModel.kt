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
package com.hilingual.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.analytics.UserIdentityTracker
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.auth.repository.AuthRepository
import com.hilingual.data.onboarding.repository.OnboardingRepository
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
    private val userRepository: UserRepository,
    private val userIdentityTracker: UserIdentityTracker,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onGoogleSignClick(context: Context) {
        if (_isLoading.value) return

        viewModelScope.launch {
            setIsLoading(true)

            authRepository.signInWithGoogle(context)
                .onSuccess { idToken ->
                    Timber.d("Google ID Token: $idToken")
                    loginWithProviderToken(idToken)
                }
                .onLogFailure {
                    showGoogleLoginErrorDialog(context)
                }

            setIsLoading(false)
        }
    }

    private suspend fun loginWithProviderToken(providerToken: String) {
        authRepository.login(providerToken)
            .onSuccess { authResult ->
                onLoginSuccess(
                    isRegistered = authResult.registerStatus,
                    userId = authResult.userId,
                )
            }
            .onLogFailure {
                showServerLoginErrorDialog(providerToken)
            }
    }

    private suspend fun onLoginSuccess(isRegistered: Boolean, userId: Long) {
        if (isRegistered) {
            updateIsSplashOnboardingCompleted()
            putDeviceInfo()
            setUserIdentity(userId)
            syncFcmToken()
            _navigationEvent.tryEmit(AuthSideEffect.NavigateToHome)
        } else {
            _navigationEvent.tryEmit(AuthSideEffect.NavigateToSignUp)
        }
    }

    private suspend fun putDeviceInfo(): Boolean =
        userRepository.putDeviceInfo().onLogFailure { }.isSuccess

    private fun syncFcmToken() {
        viewModelScope.launch {
            userRepository.syncFcmToken().onLogFailure { }
        }
    }

    private suspend fun updateIsSplashOnboardingCompleted() {
        onboardingRepository.completeSplashOnboarding()
            .onLogFailure { }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _isLoading.update { isLoading }
    }

    private fun retryLoginWithProviderToken(providerToken: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            setIsLoading(true)
            loginWithProviderToken(providerToken)
            setIsLoading(false)
        }
    }

    private suspend fun showGoogleLoginErrorDialog(context: Context) {
        _navigationEvent.emit(AuthSideEffect.ShowErrorDialog { onGoogleSignClick(context) })
    }

    private suspend fun showServerLoginErrorDialog(providerToken: String) {
        _navigationEvent.emit(AuthSideEffect.ShowErrorDialog { retryLoginWithProviderToken(providerToken) })
    }

    private fun setUserIdentity(userId: Long) {
        userIdentityTracker.setUserId(userId)
    }
}

sealed interface AuthSideEffect {
    data object NavigateToHome : AuthSideEffect
    data object NavigateToSignUp : AuthSideEffect
    data class ShowErrorDialog(val onRetry: () -> Unit) : AuthSideEffect
}
