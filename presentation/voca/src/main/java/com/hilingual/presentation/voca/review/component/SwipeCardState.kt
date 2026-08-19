package com.hilingual.presentation.voca.review.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal const val SWIPE_COMMIT_THRESHOLD_RATIO = 0.25f
internal const val SWIPE_MAX_ROTATION_DEGREES = 12f
internal const val SWIPE_VERTICAL_FOLLOW_RATIO = 0.35f
private const val TINT_START_PROGRESS = 0.15f
private const val FLY_DURATION_MILLIS = 320
private const val SNAP_BACK_DURATION_MILLIS = 280
private const val NEXT_CARD_FADE_MILLIS = 180

private val SnapBackEasing = CubicBezierEasing(0.2f, 0.8f, 0.3f, 1f)

@Stable
internal class SwipeCardState(
    private val scope: CoroutineScope,
) {
    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val cardAlpha = Animatable(1f)
    val stampAlpha = Animatable(0f)
    val contentAlpha = Animatable(1f)

    var isRightSwipe by mutableStateOf(true)
        private set

    var isBusy by mutableStateOf(false)
        private set

    internal var containerWidthPx = 0f
    internal var cardWidthPx = 0f
    internal var onJudge: ((Boolean) -> Unit)? = null
    internal var onDismissed: (() -> Unit)? = null

    fun thresholdPx(): Float = (cardWidthPx * SWIPE_COMMIT_THRESHOLD_RATIO).coerceAtLeast(1f)

    fun rotationZ(): Float =
        (offsetX.value / thresholdPx()).coerceIn(-1f, 1f) * SWIPE_MAX_ROTATION_DEGREES

    fun tintProgress(): Float {
        val progress = (abs(offsetX.value) / thresholdPx()).coerceIn(0f, 1f)
        return ((progress - TINT_START_PROGRESS) / (1f - TINT_START_PROGRESS)).coerceIn(0f, 1f)
    }

    fun commit(isMemorized: Boolean) {
        if (isBusy) return
        isBusy = true
        isRightSwipe = isMemorized
        onJudge?.invoke(isMemorized)

        scope.launch {
            stampAlpha.snapTo(1f)
            val direction = if (isMemorized) 1f else -1f
            val flySpec = tween<Float>(FLY_DURATION_MILLIS, easing = LinearOutSlowInEasing)
            coroutineScope {
                launch { offsetX.animateTo(direction * (containerWidthPx + cardWidthPx), flySpec) }
                launch { cardAlpha.animateTo(0f, flySpec) }
            }
            onDismissed?.invoke()
        }
    }

    internal fun dragTo(totalX: Float, totalY: Float) {
        if (totalX != 0f) isRightSwipe = totalX > 0f
        scope.launch {
            offsetX.snapTo(totalX)
            offsetY.snapTo(totalY * SWIPE_VERTICAL_FOLLOW_RATIO)
        }
    }

    internal fun showStamp() {
        scope.launch { stampAlpha.snapTo(1f) }
    }

    internal fun cancelDrag() {
        scope.launch {
            offsetX.snapTo(0f)
            offsetY.snapTo(0f)
        }
    }

    internal fun snapBack() {
        scope.launch {
            val snapSpec = tween<Float>(SNAP_BACK_DURATION_MILLIS, easing = SnapBackEasing)
            coroutineScope {
                launch { offsetX.animateTo(0f, snapSpec) }
                launch { offsetY.animateTo(0f, snapSpec) }
                launch { stampAlpha.animateTo(0f, snapSpec) }
            }
        }
    }

    internal suspend fun prepareNextCard() {
        offsetX.snapTo(0f)
        offsetY.snapTo(0f)
        stampAlpha.snapTo(0f)
        cardAlpha.snapTo(1f)
        contentAlpha.snapTo(0f)
        isBusy = false
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(NEXT_CARD_FADE_MILLIS, easing = LinearOutSlowInEasing),
        )
    }
}

@Composable
internal fun rememberSwipeCardState(): SwipeCardState {
    val scope = rememberCoroutineScope()
    return remember { SwipeCardState(scope) }
}
