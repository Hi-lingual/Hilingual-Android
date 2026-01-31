package com.hilingual.core.common.provider

import androidx.compose.runtime.staticCompositionLocalOf
import com.hilingual.core.common.app.AppRestarter

val LocalAppRestarter = staticCompositionLocalOf<AppRestarter> {
    error("No AppRestarter provided")
}
