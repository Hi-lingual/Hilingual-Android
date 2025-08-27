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
internal class MypageViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MypageUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<MypageUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MypageSideEffect>()
    val sideEffect: SharedFlow<MypageSideEffect> = _sideEffect.asSharedFlow()

    fun loadInitialData() {
        _uiState.value = UiState.Success(
            MypageUiState(
                profileImageUrl = "",
                profileNickname = "임시 닉네임"
            )
        )
    }
}

sealed interface MypageSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : MypageSideEffect
}
