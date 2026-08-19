package com.hilingual.presentation.voca.review.component

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.hilingual.core.common.extension.dropShadow
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.review.ReviewCardUiModel
import kotlin.math.abs
import kotlin.math.hypot

private const val CARD_ASPECT_RATIO = 0.82f
private const val TAP_SLOP_DP = 4
private const val AXIS_LOCK_DP = 10
private const val VELOCITY_THRESHOLD_DP = 600
private const val MIN_FLING_DISTANCE_DP = 8
private const val FLIP_DURATION_MILLIS = 200

@Composable
internal fun ReviewCardSection(
    card: ReviewCardUiModel,
    behindCardCount: Int,
    onFlip: () -> Unit,
    onJudge: (Boolean) -> Unit,
    onCardDismissed: () -> Unit,
    onTtsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val swipeCardState = rememberSwipeCardState()
    var isFlipped by remember { mutableStateOf(false) }
    val flipRotation = remember { Animatable(0f) }
    val isFrontVisible by remember { derivedStateOf { flipRotation.value <= 90f } }

    LaunchedEffect(isFlipped) {
        flipRotation.animateTo(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = FLIP_DURATION_MILLIS, easing = FastOutSlowInEasing),
        )
    }

    val updatedOnFlip by rememberUpdatedState(onFlip)
    val updatedOnJudge by rememberUpdatedState(onJudge)
    val updatedOnCardDismissed by rememberUpdatedState(onCardDismissed)

    SideEffect {
        swipeCardState.onJudge = updatedOnJudge
        swipeCardState.onDismissed = updatedOnCardDismissed
    }

    LaunchedEffect(card.phraseId) {
        isFlipped = false
        flipRotation.snapTo(0f)
        swipeCardState.prepareNextCard()
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .onSizeChanged { swipeCardState.containerWidthPx = it.width.toFloat() },
            contentAlignment = Alignment.TopCenter,
        ) {
            if (behindCardCount >= 2) {
                BehindCard(verticalOffset = 26.dp)
            }
            if (behindCardCount >= 1) {
                BehindCard(verticalOffset = 12.dp)
            }

            FlashCard(
                card = card,
                flipRotation = { flipRotation.value },
                isFrontVisible = isFrontVisible,
                isRightSwipe = swipeCardState.isRightSwipe,
                contentAlpha = { swipeCardState.contentAlpha.value },
                tintProgress = swipeCardState::tintProgress,
                stampAlpha = { swipeCardState.stampAlpha.value },
                onTtsClick = { onTtsClick(card.phrase) },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CARD_ASPECT_RATIO)
                    .onSizeChanged { swipeCardState.cardWidthPx = it.width.toFloat() }
                    .graphicsLayer {
                        translationX = swipeCardState.offsetX.value
                        translationY = swipeCardState.offsetY.value
                        rotationZ = swipeCardState.rotationZ()
                        alpha = swipeCardState.cardAlpha.value
                    }
                    .swipeCardGestures(
                        key = card.phraseId,
                        state = swipeCardState,
                        view = view,
                        onTap = {
                            isFlipped = !isFlipped
                            updatedOnFlip()
                        },
                    ),
            )
        }

        ReviewAnswerButtons(
            onUnknownClick = { swipeCardState.commit(isMemorized = false) },
            onKnownClick = { swipeCardState.commit(isMemorized = true) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

private fun Modifier.swipeCardGestures(
    key: Any,
    state: SwipeCardState,
    view: View,
    onTap: () -> Unit,
): Modifier = pointerInput(key) {
    val tapSlopPx = TAP_SLOP_DP.dp.toPx()
    val axisLockPx = AXIS_LOCK_DP.dp.toPx()
    val velocityThresholdPx = VELOCITY_THRESHOLD_DP.dp.toPx()
    val minFlingDistancePx = MIN_FLING_DISTANCE_DP.dp.toPx()

    awaitEachGesture {
        val down = awaitFirstDown()
        if (state.isBusy) return@awaitEachGesture

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
            if (state.isBusy) return@awaitEachGesture
            velocityTracker.addPointerInputChange(change)
            if (!change.pressed) break

            val delta = change.positionChange()
            totalX += delta.x
            totalY += delta.y
            val distance = hypot(totalX, totalY)

            if (!isAxisLockedX && !isAxisLockedY && distance >= axisLockPx) {
                if (abs(totalX) >= abs(totalY)) {
                    isAxisLockedX = true
                    state.showStamp()
                } else {
                    isAxisLockedY = true
                    state.cancelDrag()
                }
            }
            if (isAxisLockedY) continue
            if (!isAxisLockedX && distance < tapSlopPx) continue

            change.consume()
            state.dragTo(totalX, totalY)

            if (isAxisLockedX) {
                if (abs(totalX) >= state.thresholdPx()) {
                    if (!isBuzzed) {
                        isBuzzed = true
                        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    }
                } else {
                    isBuzzed = false
                }
            }
        }

        if (state.isBusy) return@awaitEachGesture

        when {
            !isAxisLockedX && hypot(totalX, totalY) < tapSlopPx -> onTap()

            !isAxisLockedX -> state.snapBack()

            else -> {
                val velocityX = velocityTracker.calculateVelocity().x
                val isOverThreshold = abs(totalX) >= state.thresholdPx()
                val isFling = abs(velocityX) > velocityThresholdPx &&
                    abs(totalX) > minFlingDistancePx

                if (isOverThreshold || isFling) {
                    state.commit(totalX > 0f)
                } else {
                    state.snapBack()
                }
            }
        }
    }
}

@Composable
private fun BehindCard(
    verticalOffset: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(CARD_ASPECT_RATIO)
            .offset(y = verticalOffset)
            .dropShadow(
                shape = RoundedCornerShape(20.dp),
                color = HilingualTheme.colors.black,
                alpha = 0.08f,
                offsetX = 0.dp,
                offsetY = 2.dp,
                blur = 8.dp,
                spread = 0.dp,
            )
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(20.dp),
            ),
    )
}
