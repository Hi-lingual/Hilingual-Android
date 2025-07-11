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
val hilingualBlack = Color(0xFF212121)
val hilingualOrange = Color(0xFFFF6937)
val hilingualBlue = Color(0xFF487AFF)

val nounBg = Color(0xFFCAD8FF)
val pronounBg = Color(0xFF5642EB)
val adjBg = Color(0xFFFFBAD3)
val adverbBg = Color(0xFFC5FF87)
val prepositionBg = Color(0xFFFFEC8E)
val interjectionBg = Color(0xFFCD33D3)
val pronounText = Color(0xFFDCD7FF)
val adjText = Color(0xFFE81879)
val adverbText = Color(0xFF089900)

val black = Color(0xFF121212)
val gray850 = Color(0xFF212121)
val gray700 = Color(0xFF424242)
val gray500 = Color(0xFF757575)
val gray400 = Color(0xFF9E9E9E)
val gray300 = Color(0xFFBDBDBD)
val gray200 = Color(0xFFE0E0E0)
val gray100 = Color(0xFFF5F5F5)
val white = Color(0xFFFFFFFF)
val dim = Color(0xFF000000).copy(alpha = 0.7f)

val alertRed = Color(0xFFFF3B30)
val infoBlue = Color(0xFF007AFF)

@Stable
class HilingualColors(
    hilingualBlack: Color,
    hilingualOrange: Color,
    hilingualBlue: Color,
    nounBg: Color,
    pronounBg: Color,
    adjBg: Color,
    adverbBg: Color,
    prepositionBg: Color,
    interjectionBg: Color,
    pronounText: Color,
    adjText: Color,
    adverbText: Color,
    black: Color,
    gray850: Color,
    gray700: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    white: Color,
    dim: Color,
    alertRed: Color,
    infoBlue: Color

) {
    var hilingualBlack by mutableStateOf(hilingualBlack)
        private set
    var hilingualOrange by mutableStateOf(hilingualOrange)
        private set
    var hilingualBlue by mutableStateOf(hilingualBlue)
        private set
    var nounBg by mutableStateOf(nounBg)
        private set
    var pronounBg by mutableStateOf(pronounBg)
        private set
    var adjBg by mutableStateOf(adjBg)
        private set
    var adverbBg by mutableStateOf(adverbBg)
        private set
    var prepositionBg by mutableStateOf(prepositionBg)
        private set
    var interjectionBg by mutableStateOf(interjectionBg)
        private set
    var pronounText by mutableStateOf(pronounText)
        private set
    var adjText by mutableStateOf(adjText)
        private set
    var adverbText by mutableStateOf(adverbText)
        private set
    var black by mutableStateOf(black)
        private set
    var gray850 by mutableStateOf(gray850)
        private set
    var gray700 by mutableStateOf(gray700)
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
    var dim by mutableStateOf(dim)
        private set
    var alertRed by mutableStateOf(alertRed)
        private set
    var infoBlue by mutableStateOf(infoBlue)
        private set

    fun copy(): HilingualColors = HilingualColors(
        hilingualBlack,
        hilingualOrange,
        hilingualBlue,
        nounBg,
        pronounBg,
        adjBg,
        adverbBg,
        prepositionBg,
        interjectionBg,
        pronounText,
        adjText,
        adverbText,
        black,
        gray850,
        gray700,
        gray500,
        gray400,
        gray300,
        gray200,
        gray100,
        white,
        dim,
        alertRed,
        infoBlue
    )

    fun update(other: HilingualColors) {
        hilingualBlack = other.hilingualBlack
        hilingualOrange = other.hilingualOrange
        hilingualBlue = other.hilingualBlue
        nounBg = other.nounBg
        pronounBg = other.pronounBg
        adjBg = other.adjBg
        adverbBg = other.adverbBg
        prepositionBg = other.prepositionBg
        interjectionBg = other.interjectionBg
        pronounText = other.pronounText
        adjText = other.adjText
        adverbText = other.adverbText
        black = other.black
        gray850 = other.gray850
        gray700 = other.gray700
        gray500 = other.gray500
        gray400 = other.gray400
        gray300 = other.gray300
        gray200 = other.gray200
        gray100 = other.gray100
        white = other.white
        dim = other.dim
        alertRed = other.alertRed
        infoBlue = other.infoBlue
    }
}

fun DefaultHilingualColors(
    HilingualBlack: Color = hilingualBlack,
    HilingualOrange: Color = hilingualOrange,
    HilingualBlue: Color = hilingualBlue,
    NounBg: Color = nounBg,
    PronounBg: Color = pronounBg,
    AdjBg: Color = adjBg,
    AdverbBg: Color = adverbBg,
    PrepositionBg: Color = prepositionBg,
    InterjectionBg: Color = interjectionBg,
    PronounText: Color = pronounText,
    AdjText: Color = adjText,
    AdverbText: Color = adverbText,
    Black: Color = black,
    Gray850: Color = gray850,
    Gray700: Color = gray700,
    Gray500: Color = gray500,
    Gray400: Color = gray400,
    Gray300: Color = gray300,
    Gray200: Color = gray200,
    Gray100: Color = gray100,
    White: Color = white,
    Dim: Color = dim,
    AlertRed: Color = alertRed,
    InfoBlue: Color = infoBlue
) = HilingualColors(
    HilingualBlack,
    HilingualOrange,
    HilingualBlue,
    NounBg,
    PronounBg,
    AdjBg,
    AdverbBg,
    PrepositionBg,
    InterjectionBg,
    PronounText,
    AdjText,
    AdverbText,
    Black,
    Gray850,
    Gray700,
    Gray500,
    Gray400,
    Gray300,
    Gray200,
    Gray100,
    White,
    Dim,
    AlertRed,
    InfoBlue
)

@Preview(showBackground = true)
@Composable
fun HilingualMainColorsPreview() {
    HilingualTheme {
        Column {
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.hilingualOrange,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.black
                )
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.hilingualBlue,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.black
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HilingualWordChipColorsPreview() {
    HilingualTheme {
        Column {
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.nounBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.pronounBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.adjBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.adverbBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.prepositionBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.interjectionBg
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.pronounText
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.adjText
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.adverbText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HilingualGrayColorsPreview() {
    HilingualTheme {
        Column {
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.black
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray850
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray700
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray500
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray400
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray300
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray200
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.gray100
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.white,
                modifier = Modifier.background(
                    color = HilingualTheme.colors.black
                )

            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.dim

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private
fun HilingualSymenticColorsPreview() {
    HilingualTheme {
        Column {
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.alertRed
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.infoBlue
            )
        }
    }
}
