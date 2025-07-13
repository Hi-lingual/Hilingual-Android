package com.hilingual.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
                    authRepository.login(idToken, "GOOGLE")
                        .onSuccess { authResult ->
                            if (authResult.isProfileCompleted) {
                                _navigationEvent.tryEmit(AuthSideEffect.NavigateToHome)
                            } else {
                                _navigationEvent.tryEmit(AuthSideEffect.NavigateToOnboarding)
                            }
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
