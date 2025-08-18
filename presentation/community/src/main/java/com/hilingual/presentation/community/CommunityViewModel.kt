package com.hilingual.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CommunityUiEvent {
    data class ShowSnackbar(val message: String, val actionLabel: String) : CommunityUiEvent
}

@HiltViewModel
class CommunityViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<CommunityUiEvent>()
    val event = _event.asSharedFlow()

    fun showSnackbar1() {
        viewModelScope.launch {
            _event.emit(CommunityUiEvent.ShowSnackbar(message = "첫 번째 스낵바입니다.", actionLabel = "버튼 1"))
        }
    }

    fun showSnackbar2() {
        viewModelScope.launch {
            _event.emit(CommunityUiEvent.ShowSnackbar(message = "두 번째 스낵바입니다.", actionLabel = "버튼 2"))
        }
    }
}
