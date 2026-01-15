package com.hilingual.core.common.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.hilingual.core.common.model.HilingualMessage

val LocalMessageController = staticCompositionLocalOf<(HilingualMessage) -> Unit> {
    error("No MessageController provided")
}
