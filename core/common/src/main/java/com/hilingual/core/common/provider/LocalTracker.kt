package com.hilingual.core.common.provider

import androidx.compose.runtime.staticCompositionLocalOf
import com.hilingual.core.common.analytics.Tracker

val LocalTracker = staticCompositionLocalOf<Tracker> {
    error("No Tracker provided")
}
