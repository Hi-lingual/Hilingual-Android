package com.hilingual.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _navigationEvent = Channel<AuthSideEffect>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onGoogleSignClick() {
        viewModelScope.launch {
            authRepository.signInWithGoogle(context)
                .onSuccess { credential ->
                    Timber.d(credential.idToken)
                    _navigationEvent.send(AuthSideEffect.NavigateToHome)
                }
                .onLogFailure { }
        }
    }
}

sealed interface AuthSideEffect {
    data object NavigateToHome : AuthSideEffect
    data object NavigateToOnboarding : AuthSideEffect
}