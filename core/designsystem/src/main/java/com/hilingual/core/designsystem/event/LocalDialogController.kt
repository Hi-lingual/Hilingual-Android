package com.hilingual.core.designsystem.event

import androidx.compose.runtime.staticCompositionLocalOf

val LocalDialogController = staticCompositionLocalOf<DialogController> {
    error("No DialogController provided")
}
