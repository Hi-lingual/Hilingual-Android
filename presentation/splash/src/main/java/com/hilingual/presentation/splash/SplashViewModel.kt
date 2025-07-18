package com.hilingual.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.localstorage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.NotLoggedIn)
    val uiState = _uiState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
            val isProfileCompleted = tokenManager.isProfileCompleted()

            _uiState.value = if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                if (isProfileCompleted) {
                    SplashUiState.LoggedIn
                } else {
                    SplashUiState.OnboardingRequired
                }
            } else {
                SplashUiState.NotLoggedIn
            }
        }
    }
}

sealed interface SplashUiState {
    data object LoggedIn : SplashUiState
    data object NotLoggedIn : SplashUiState
    data object OnboardingRequired : SplashUiState
}
