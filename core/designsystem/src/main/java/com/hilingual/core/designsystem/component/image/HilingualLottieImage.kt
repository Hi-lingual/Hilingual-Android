package com.hilingual.core.designsystem.component.image

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun HilingualLottieAnimation(
    @RawRes rawResFile: Int,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Fit,
    speed: Float = 1f,
    iterations: Int = 1,
    isInfinite: Boolean = false
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawResFile))
    LottieAnimation(
        modifier = modifier.clip(shape),
        speed = speed,
        composition = lottieComposition,
        iterations = if (isInfinite) LottieConstants.IterateForever else iterations,
        contentScale = contentScale,
        clipToCompositionBounds = false
    )
}