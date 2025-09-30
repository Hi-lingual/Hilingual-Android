package com.hilingual.core.common.provider

import androidx.compose.runtime.compositionLocalOf
import com.hilingual.core.common.analytics.Tracker

val LocalTracker = compositionLocalOf<Tracker> {
    error("No Tracker provided")
}
