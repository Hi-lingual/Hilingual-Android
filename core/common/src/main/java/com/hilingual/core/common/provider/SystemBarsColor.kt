package com.hilingual.core.common.provider

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

        window.statusBarColor = localSystemBarsColor.statusBarColor.toArgb()

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
