package com.hilingual.presentation.diarywrite.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import com.hilingual.presentation.diarywrite.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
internal fun AnimatedLoadingLottie(
    height: Dp,
    modifier: Modifier = Modifier
) {
    val lottieFiles = remember {
        persistentListOf(
            R.raw.lottie_feedback_loading_1,
            R.raw.lottie_feedback_loading_2,
            R.raw.lottie_feedback_loading_3
        )
    }
    var currentIndex by remember { mutableIntStateOf(0) }
    val transition = rememberInfiniteTransition(label = "lottie fade transition")

    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lottie alpha"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % lottieFiles.size
        }
    }

    HilingualLottieAnimation(
        modifier = modifier
            .width(200.dp)
            .height(height)
            .alpha(alpha),
        rawResFile = lottieFiles[currentIndex],
        isInfinite = true
    )
}
