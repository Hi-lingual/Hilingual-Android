package com.hilingual.presentation.feedprofile.follow

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class FollowListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FollowListUiState.Fake)
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()
}
