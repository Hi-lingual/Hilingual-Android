package com.hilingual.presentation.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.common.provider.SystemBarsColorController
import com.hilingual.core.designsystem.theme.HilingualTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var systemBarsColor: SystemBarsColorController

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            HilingualTheme {
                CompositionLocalProvider(LocalSystemBarsColor provides systemBarsColor) {
                    MainScreen()
                }
            }
        }
    }
}
