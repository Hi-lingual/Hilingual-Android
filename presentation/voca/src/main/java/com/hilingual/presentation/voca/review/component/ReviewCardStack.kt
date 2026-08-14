package com.hilingual.presentation.voca.review.component

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.review.ReviewCardUiModel
import kotlin.math.abs
import kotlin.math.hypot
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val COMMIT_THRESHOLD_RATIO = 0.25f
private const val MAX_ROTATION_DEGREES = 12f
private const val VERTICAL_FOLLOW_RATIO = 0.35f
private const val TAP_SLOP_DP = 4
private const val AXIS_LOCK_DP = 10
private const val VELOCITY_THRESHOLD_DP = 600
private const val MIN_FLING_DISTANCE_DP = 8
private const val FLIP_DURATION_MILLIS = 200
private const val FLY_DURATION_MILLIS = 320
private const val SNAP_BACK_DURATION_MILLIS = 280
private const val NEXT_CARD_FADE_MILLIS = 180

private val SnapBackEasing = CubicBezierEasing(0.2f, 0.8f, 0.3f, 1f)

@Stable
internal class ReviewCardController {
    internal var onCommit: ((Boolean) -> Unit)? = null

    fun commit(isMemorized: Boolean) {
        onCommit?.invoke(isMemorized)
    }
}

@Composable
internal fun rememberReviewCardController(): ReviewCardController = remember { ReviewCardController() }

@Composable
internal fun ReviewCardStack(
    card: ReviewCardUiModel,
    behindCardCount: Int,
    controller: ReviewCardController,
    onFlip: () -> Unit,
    onResult: (Boolean) -> Unit,
    onTtsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val view = LocalView.current

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val cardAlpha = remember { Animatable(1f) }
    val stampAlpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(1f) }

    var containerWidthPx by remember { mutableFloatStateOf(0f) }
    var cardWidthPx by remember { mutableFloatStateOf(0f) }
    var isBusy by remember { mutableStateOf(false) }
    var isRightSwipe by remember { mutableStateOf(true) }
    var isFlipped by remember { mutableStateOf(false) }

    val flipRotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = FLIP_DURATION_MILLIS, easing = FastOutSlowInEasing),
        label = "flipRotation",
    )

    val updatedOnFlip by rememberUpdatedState(onFlip)
    val updatedOnResult by rememberUpdatedState(onResult)

    fun thresholdPx(): Float = (cardWidthPx * COMMIT_THRESHOLD_RATIO).coerceAtLeast(1f)

    LaunchedEffect(card.phraseId) {
        isFlipped = false
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

    fun commit(isMemorized: Boolean) {
        if (isBusy) return
        isBusy = true
        isRightSwipe = isMemorized

        scope.launch {
            stampAlpha.snapTo(1f)
            val direction = if (isMemorized) 1f else -1f
            val flyDistance = direction * (containerWidthPx + cardWidthPx)
            val flySpec = tween<Float>(FLY_DURATION_MILLIS, easing = LinearOutSlowInEasing)
            coroutineScope {
                launch { offsetX.animateTo(flyDistance, flySpec) }
                launch { cardAlpha.animateTo(0f, flySpec) }
            }
            updatedOnResult(isMemorized)
        }
    }

    SideEffect { controller.onCommit = ::commit }

    suspend fun snapBack() {
        val snapSpec = tween<Float>(SNAP_BACK_DURATION_MILLIS, easing = SnapBackEasing)
        coroutineScope {
            launch { offsetX.animateTo(0f, snapSpec) }
            launch { offsetY.animateTo(0f, snapSpec) }
            launch { stampAlpha.animateTo(0f, snapSpec) }
        }
    }

    Box(
        modifier = modifier.onSizeChanged { containerWidthPx = it.width.toFloat() },
        contentAlignment = Alignment.TopCenter,
    ) {
        if (behindCardCount >= 2) {
            BehindCard(verticalOffset = 16.dp, horizontalInset = 16.dp)
        }
        if (behindCardCount >= 1) {
            BehindCard(verticalOffset = 8.dp, horizontalInset = 8.dp)
        }

        FlashCard(
            card = card,
            flipRotation = flipRotation,
            isRightSwipe = isRightSwipe,
            contentAlpha = { contentAlpha.value },
            tintProgress = {
                val progress = (abs(offsetX.value) / thresholdPx()).coerceIn(0f, 1f)
                ((progress - 0.15f) / 0.85f).coerceIn(0f, 1f)
            },
            stampAlpha = { stampAlpha.value },
            onTtsClick = { onTtsClick(card.phrase) },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.82f)
                .onSizeChanged { cardWidthPx = it.width.toFloat() }
                .graphicsLayer {
                    translationX = offsetX.value
                    translationY = offsetY.value
                    rotationZ = (offsetX.value / thresholdPx()).coerceIn(-1f, 1f) * MAX_ROTATION_DEGREES
                    alpha = cardAlpha.value
                }
                .pointerInput(card.phraseId) {
                    val tapSlopPx = TAP_SLOP_DP.dp.toPx()
                    val axisLockPx = AXIS_LOCK_DP.dp.toPx()
                    val velocityThresholdPx = VELOCITY_THRESHOLD_DP.dp.toPx()
                    val minFlingDistancePx = MIN_FLING_DISTANCE_DP.dp.toPx()

                    awaitEachGesture {
                        val down = awaitFirstDown()
                        if (isBusy) return@awaitEachGesture

                        val velocityTracker = VelocityTracker()
                        velocityTracker.addPointerInputChange(down)
                        var totalX = 0f
                        var totalY = 0f
                        var isAxisLockedX = false
                        var isAxisLockedY = false
                        var isBuzzed = false

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            velocityTracker.addPointerInputChange(change)
                            if (!change.pressed) break

                            val delta = change.positionChange()
                            totalX += delta.x
                            totalY += delta.y
                            val distance = hypot(totalX, totalY)

                            if (!isAxisLockedX && !isAxisLockedY && distance >= axisLockPx) {
                                if (abs(totalX) >= abs(totalY)) {
                                    isAxisLockedX = true
                                    scope.launch { stampAlpha.snapTo(1f) }
                                } else {
                                    isAxisLockedY = true
                                    scope.launch {
                                        offsetX.snapTo(0f)
                                        offsetY.snapTo(0f)
                                    }
                                }
                            }
                            if (isAxisLockedY) continue
                            if (!isAxisLockedX && distance < tapSlopPx) continue

                            change.consume()
                            if (totalX != 0f) isRightSwipe = totalX > 0f
                            scope.launch {
                                offsetX.snapTo(totalX)
                                offsetY.snapTo(totalY * VERTICAL_FOLLOW_RATIO)
                            }

                            if (isAxisLockedX) {
                                if (abs(totalX) >= thresholdPx()) {
                                    if (!isBuzzed) {
                                        isBuzzed = true
                                        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                    }
                                } else {
                                    isBuzzed = false
                                }
                            }
                        }

                        if (isBusy) return@awaitEachGesture

                        when {
                            !isAxisLockedX && hypot(totalX, totalY) < tapSlopPx -> {
                                isFlipped = !isFlipped
                                updatedOnFlip()
                            }

                            !isAxisLockedX -> scope.launch { snapBack() }

                            else -> {
                                val velocityX = velocityTracker.calculateVelocity().x
                                val isOverThreshold = abs(totalX) >= thresholdPx()
                                val isFling = abs(velocityX) > velocityThresholdPx &&
                                    abs(totalX) > minFlingDistancePx

                                if (isOverThreshold || isFling) {
                                    commit(totalX > 0f)
                                } else {
                                    scope.launch { snapBack() }
                                }
                            }
                        }
                    }
                },
        )
    }
}

@Composable
private fun BehindCard(
    verticalOffset: Dp,
    horizontalInset: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.82f)
            .offset(y = verticalOffset)
            .padding(horizontal = horizontalInset)
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(20.dp),
            ),
    )
}
