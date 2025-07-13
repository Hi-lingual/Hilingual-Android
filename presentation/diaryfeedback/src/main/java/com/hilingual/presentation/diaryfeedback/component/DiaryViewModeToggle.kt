package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryViewModeToggle(
    isAIWritten: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "AI가 쓴 일기",
            style = HilingualTheme.typography.captionM12,
            color = HilingualTheme.colors.gray700
        )
        BasicSwitch(
            isChecked = isAIWritten,
            onCheckedChange = onToggle
        )
    }
}

@Composable
private fun BasicSwitch(
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    width: Dp = 44.dp,
    height: Dp = 22.dp,
    checkedTrackColor: Color = HilingualTheme.colors.hilingualOrange,
    uncheckedTrackColor: Color = HilingualTheme.colors.gray300,
    gapBetweenThumbAndTrackEdge: Dp = 2.dp
) {
    val halfHeight = height / 2
    val thumbRadius = halfHeight - gapBetweenThumbAndTrackEdge

    val density = LocalDensity.current
    val targetPosition = remember(isChecked) {
        with(density) {
            if (isChecked) {
                (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx()
            } else {
                (thumbRadius + gapBetweenThumbAndTrackEdge).toPx()
            }
        }
    }

    val animatePosition by animateFloatAsState(
        targetValue = targetPosition
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .pointerInput(onCheckedChange) {
                detectTapGestures(
                    onTap = { onCheckedChange() }
                )
            }
    ) {
        drawRoundRect( // Track
            color = if (isChecked) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = halfHeight.toPx(), y = halfHeight.toPx())
        )

        drawCircle( // Thumb
            color = Color.White,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition,
                y = size.height / 2
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AIDiaryTogglePreview() {
    HilingualTheme {
        var isAI by remember { mutableStateOf(true) }

        DiaryViewModeToggle(
            isAIWritten = isAI,
            onToggle = { isAI = !isAI }
        )
    }
}
