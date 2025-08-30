package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class NotificationSettingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationSettingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(isMarketingChecked = true, isFeedChecked = false)
        }
    }

    fun onMarketingCheckedChange(isChecked: Boolean) {
        _uiState.update { it.copy(isMarketingChecked = isChecked) }
    }

    fun onFeedCheckedChange(isChecked: Boolean) {
        _uiState.update { it.copy(isFeedChecked = isChecked) }
    }
}
