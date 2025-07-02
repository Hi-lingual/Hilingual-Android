package com.hilingual.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
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
fun HilingualTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = DefaultHilingualColors()
    val typography = HilingualTypography()
    ProvideHilingualColorsAndTypography(colors, typography) {
        MaterialTheme(content = content)
    }
}