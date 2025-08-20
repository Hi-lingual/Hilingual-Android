package com.hilingual.presentation.feed

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class FeedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FeedUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedUiState>> = _uiState.asStateFlow()

}