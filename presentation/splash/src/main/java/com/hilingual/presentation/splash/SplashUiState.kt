package com.hilingual.presentation.splash

import androidx.compose.runtime.Immutable
import com.hilingual.data.config.model.UpdateState

@Immutable
data class SplashUiState(
    val updateState: UpdateState = UpdateState.NONE
)
