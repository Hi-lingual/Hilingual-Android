package com.hilingual.presentation.mypage

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MyPageUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<MyPageUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MyPageSideEffect>()
    val sideEffect: SharedFlow<MyPageSideEffect> = _sideEffect.asSharedFlow()

    fun loadInitialData() {
        _uiState.value = UiState.Success(
            MyPageUiState(
                profileImageUrl = "",
                profileNickname = "임시 닉네임"
            )
        )
    }
}

sealed interface MyPageSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : MyPageSideEffect
}
