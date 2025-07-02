package com.hilingual.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

val hilingualOrange = Color(0xFFFF6937)
val hilingualBlue = Color(0xFF487AFF)

val gray900 = Color(0xFF121212)
val gray850 = Color(0xFF212121)
val gray800 = Color(0xFF333333)
val gray700 = Color(0xFF424242)
val gray600 = Color(0xFF616161)
val gray500 = Color(0xFF757575)
val gray400 = Color(0xFF9E9E9E)
val gray300 = Color(0xFFBDBDBD)
val gray200 = Color(0xFFE0E0E0)
val gray100 = Color(0xFFEEEEEE)
val white = Color(0xFFFFFFFF)

@Stable
class HilingualColors(
    hilingualBlack: Color,
    hilingualOrange: Color,
    hilingualBlue: Color,
    gray900: Color,
    gray850: Color,
    gray800: Color,
    gray700: Color,
    gray600: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    white: Color,
    isLight: Boolean

) {
    var hilingualBlack by mutableStateOf(hilingualBlack)
        private set
    var hilingualOrange by mutableStateOf(hilingualOrange)
        private set
    var hilingualBlue by mutableStateOf(hilingualBlue)
        private set
    var gray900 by mutableStateOf(gray900)
        private set
    var gray850 by mutableStateOf(gray850)
        private set
    var gray800 by mutableStateOf(gray800)
        private set
    var gray700 by mutableStateOf(gray700)
        private set
    var gray600 by mutableStateOf(gray600)
        private set
    var gray500 by mutableStateOf(gray500)
        private set
    var gray400 by mutableStateOf(gray400)
        private set
    var gray300 by mutableStateOf(gray300)
        private set
    var gray200 by mutableStateOf(gray200)
        private set
    var gray100 by mutableStateOf(gray100)
        private set
    var white by mutableStateOf(white)
        private set
    var isLight by mutableStateOf(isLight)

    fun copy(): HilingualColors = HilingualColors(
        hilingualBlack,
        hilingualOrange,
        hilingualBlue,
        gray900,
        gray850,
        gray800,
        gray700,
        gray600,
        gray500,
        gray400,
        gray300,
        gray200,
        gray100,
        white,
        isLight
    )

    fun update(other: HilingualColors) {
        hilingualBlack = other.hilingualBlack
        hilingualOrange = other.hilingualOrange
        hilingualBlue = other.hilingualBlue
        gray900 = other.gray900
        gray850 = other.gray850
        gray800 = other.gray800
        gray700 = other.gray700
        gray600 = other.gray600
        gray500 = other.gray500
        gray400 = other.gray400
        gray300 = other.gray300
        gray200 = other.gray200
        gray100 = other.gray100
        white = other.white
        isLight = other.isLight

    }
}

fun HilingualLightColors(
    HilingualBlack: Color = gray850,
    HilingualOrange: Color = hilingualOrange,
    HilingualBlue: Color = hilingualBlue,
    Gray900: Color = gray900,
    Gray850: Color = gray850,
    Gray800: Color = gray800,
    Gray700: Color = gray700,
    Gray600: Color = gray600,
    Gray500: Color = gray500,
    Gray400: Color = gray400,
    Gray300: Color = gray300,
    Gray200: Color = gray200,
    Gray100: Color = gray100,
    White: Color = white
) = HilingualColors(
    HilingualBlack,
    HilingualOrange,
    HilingualBlue,
    Gray900,
    Gray850,
    Gray800,
    Gray700,
    Gray600,
    Gray500,
    Gray400,
    Gray300,
    Gray200,
    Gray100,
    White,
    isLight = true
)

@Preview(showBackground = true)
@Composable
fun HilingualMainColorsPreview(){
    HilingualTheme {
        Column {
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.hilingualBlack
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.hilingualOrange,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.hilingualBlack
                )
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.hilingualBlue,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.hilingualBlack
                )
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HilingualGrayColorsPreview(){
    HilingualTheme{
        Column {
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray900
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray850
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray800
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray700
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray600
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray500
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray400
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray300
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray200
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray100
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.white,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.hilingualBlack
                )
            )
        }
    }
}