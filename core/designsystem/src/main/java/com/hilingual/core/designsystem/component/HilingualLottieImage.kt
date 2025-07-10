package com.hilingual.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun HilingualLottieAnimation(
    jsonFile: Int,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Fit,
    speed: Float = 1f,
    iterations: Int = 1,
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(jsonFile))
    LottieAnimation(
        modifier = modifier.clip(shape),
        speed = speed,
        composition = lottieComposition,
        iterations = iterations,
        contentScale = contentScale,
        clipToCompositionBounds = false
    )
}