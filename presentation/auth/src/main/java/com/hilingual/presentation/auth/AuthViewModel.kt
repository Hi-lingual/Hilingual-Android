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
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.auth.repository.AuthRepository
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<AuthSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onGoogleSignClick(context: Context) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(context)
                .onSuccess { idToken ->
                    Timber.d("Google ID Token: $idToken")
                    authRepository.login(idToken)
                        .onSuccess { authResult ->
                            val sideEffect = if (authResult.registerStatus) {
                                AuthSideEffect.NavigateToHome
                            } else {
                                AuthSideEffect.NavigateToOnboarding
                            }
                            _navigationEvent.tryEmit(sideEffect)
                        }
                        .onLogFailure { }
                }
                .onLogFailure { }
        }
    }
}

sealed interface AuthSideEffect {
    data object NavigateToHome : AuthSideEffect
    data object NavigateToOnboarding : AuthSideEffect
}
