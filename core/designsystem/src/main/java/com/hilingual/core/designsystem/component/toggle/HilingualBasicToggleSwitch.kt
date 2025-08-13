package com.hilingual.core.designsystem.component.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicToggleSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 52.dp,
    height: Dp = 28.dp,
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
        modifier = modifier
            .size(width = width, height = height)
            .toggleable(
                value = isChecked,
                onValueChange = { onCheckedChange(it) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
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
private fun ToggleSwitchPreview() {
    HilingualTheme {
        var isChecked by remember { mutableStateOf(true) }

        HilingualBasicToggleSwitch(
            isChecked = isChecked,
            onCheckedChange = { isChecked = !isChecked }
        )
    }
}
