/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.core.common.event

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import javax.inject.Inject

val LocalSystemBarsColor = staticCompositionLocalOf<SystemBarsColorController> {
    error("No SystemBarsColor provided")
}

class SystemBarsColorController @Inject constructor() {
    private var localSystemBarsColor by mutableStateOf(SystemBarsColor())

    fun setSystemBarColor(
        systemBarsColor: Color,
        isDarkIcon: Boolean = true
    ) {
        localSystemBarsColor = localSystemBarsColor.copy(
            statusBarColor = systemBarsColor,
            isDarkIcon = isDarkIcon
        )
    }

    @Composable
    fun Apply(activity: Activity) {
        val window = activity.window
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = localSystemBarsColor.isDarkIcon

        window.setStatusBarColor(localSystemBarsColor.statusBarColor.toArgb())

        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusBarHeight)
                    .background(color = localSystemBarsColor.statusBarColor)
            )
        }
    }
}

private data class SystemBarsColor(
    val statusBarColor: Color = Color.White,
    val isDarkIcon: Boolean = true
)
