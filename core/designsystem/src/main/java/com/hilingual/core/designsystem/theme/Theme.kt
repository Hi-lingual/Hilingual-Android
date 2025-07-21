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
package com.hilingual.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalHilingualColors = staticCompositionLocalOf<HilingualColors> {
    error("No HilingualColors provided")
}
private val LocalHilingualTypography = staticCompositionLocalOf<HilingualTypography> {
    error("No HilingualTypography provided")
}

private val LightColorScheme = lightColorScheme(
    primary = hilingualBlack,
    background = white
)

object HilingualTheme {
    val colors: HilingualColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHilingualColors.current

    val typography: HilingualTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalHilingualTypography.current
}

@Composable
fun ProvideHilingualColorsAndTypography(
    colors: HilingualColors,
    typography: HilingualTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }.apply { update(typography) }
    CompositionLocalProvider(
        LocalHilingualColors provides provideColors,
        LocalHilingualTypography provides provideTypography,
        content = content
    )
}

@Composable
fun HilingualTheme(content: @Composable () -> Unit) {
    val colors = DefaultHilingualColors()
    val colorScheme = LightColorScheme
    val typography = HilingualTypography()
    ProvideHilingualColorsAndTypography(colors, typography) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
