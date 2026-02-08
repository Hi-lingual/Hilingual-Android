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
package com.hilingual.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.provider.DeviceInfoProvider
import com.hilingual.data.auth.repository.AuthRepository
import com.hilingual.data.config.model.AppVersion
import com.hilingual.data.config.model.UpdateState
import com.hilingual.data.config.repository.ConfigRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.splash.SplashSideEffect.NavigateToAuth
import com.hilingual.presentation.splash.SplashSideEffect.NavigateToHome
import com.hilingual.presentation.splash.SplashSideEffect.NavigateToStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val configRepository: ConfigRepository,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SplashSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sideEffect = _sideEffect.asSharedFlow()

    fun checkAppVersion() {
        val currentVersion = AppVersion(deviceInfoProvider.getAppVersion())

        viewModelScope.launch {
            configRepository.getAppVersionInfo()
                .onSuccess { info ->
                    val state = info.checkUpdateStatus(currentVersion)
                    _uiState.update { it.copy(updateState = state) }

                    if (state == UpdateState.NONE) checkLoginStatus()
                }
                .onLogFailure { checkLoginStatus() }
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val isLoggedIn = runCatching {
                val accessTokenDeferred = async { authRepository.getAccessToken() }
                val refreshTokenDeferred = async { authRepository.getRefreshToken() }
                val isRegisteredDeferred = async { userRepository.getRegisterStatus() }

                val accessToken = accessTokenDeferred.await()
                val refreshToken = refreshTokenDeferred.await()
                val isRegistered = isRegisteredDeferred.await()

                !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty() && isRegistered
            }.getOrElse { false }

            delay(1400L)

            _sideEffect.tryEmit(if (isLoggedIn) NavigateToHome else NavigateToAuth)
        }
    }

    fun onUpdateConfirm() {
        _sideEffect.tryEmit(NavigateToStore)
    }

    fun onUpdateSkip() {
        _uiState.update { it.copy(updateState = UpdateState.NONE) }
        checkLoginStatus()
    }
}

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToAuth : SplashSideEffect
    data object NavigateToStore : SplashSideEffect
}
