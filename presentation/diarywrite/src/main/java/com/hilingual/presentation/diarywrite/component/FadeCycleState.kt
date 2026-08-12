package com.hilingual.presentation.diarywrite.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
internal class FadeCycleState(private val itemCount: Int) {
    var currentIndex by mutableIntStateOf(0)
        private set

    private val alphaAnimatable = Animatable(0f)

    val alpha: Float
        get() = alphaAnimatable.value

    suspend fun animateCycle() {
        while (true) {
            alphaAnimatable.animateTo(1f, tween(FADE_DURATION_MILLIS))
            alphaAnimatable.animateTo(0f, tween(FADE_DURATION_MILLIS))
            currentIndex = (currentIndex + 1) % itemCount
        }
    }

    companion object {
        private const val FADE_DURATION_MILLIS = 1500
    }
}

@Composable
internal fun rememberFadeCycleState(itemCount: Int): FadeCycleState {
    val state = remember(itemCount) { FadeCycleState(itemCount) }

    LaunchedEffect(state) {
        state.animateCycle()
    }

    return state
}
