package com.hilingual.core.common.model

import androidx.compose.runtime.Immutable

@Immutable
data class SnackbarRequest(
    val message: String,
    val buttonText: String,
    val onClick: () -> Unit
)
