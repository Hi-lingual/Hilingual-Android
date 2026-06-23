package com.hilingual.core.designsystem.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HilingualSegmentedButton(
    options: ImmutableList<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    thumbWidth: Dp = 62.dp,
    itemSpacing: Dp = 3.dp,
    contentPadding: PaddingValues = PaddingValues(3.dp),
    colors: SegmentedButtonColors = SegmentedButtonColors.defaults(),
) {
    val coercedSelectedIndex = selectedIndex.coerceIn(0, options.lastIndex)
    val animatedIndex by animateFloatAsState(
        targetValue = coercedSelectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 380f,
        ),
        label = "thumbOffset",
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colors.backgroundColor)
            .padding(contentPadding),
    ) {
        Box(  // Sliding thumb: visual indicator that moves horizontally based on selected index
            modifier = Modifier
                .matchParentSize()
                .layout { measurable, constraints ->
                    val thumbWidthPx = thumbWidth.roundToPx()
                    val itemSpacingPx = itemSpacing.roundToPx()
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = thumbWidthPx,
                            maxWidth = thumbWidthPx,
                        ),
                    )
                    layout(constraints.maxWidth, placeable.height) {
                        val x = (animatedIndex * (thumbWidthPx + itemSpacingPx)).toInt()
                        placeable.placeRelative(x, 0)
                    }
                }
                .clip(CircleShape)
                .background(colors.thumbColor),
        )

        Row {
            options.forEachIndexed { index, label ->
                val selected = index == coercedSelectedIndex
                Box(
                    modifier = Modifier
                        .width(thumbWidth)
                        .noRippleClickable(
                            onClick = { onSelect(index) },
                        )
                        .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        modifier = Modifier
                            .widthIn(min = 46.dp),
                        textAlign = TextAlign.Center,
                        color = if (selected) colors.activeContentColor else colors.inactiveContentColor,
                        style = if (selected) HilingualTheme.typography.bodyM14 else HilingualTheme.typography.bodyR14,
                    )
                }

                if (index != options.lastIndex) {
                    Spacer(modifier = Modifier.width(itemSpacing))
                }
            }
        }
    }
}

data class SegmentedButtonColors(
    val backgroundColor: Color,
    val thumbColor: Color,
    val activeContentColor: Color,
    val inactiveContentColor: Color,
) {
    companion object {
        @Composable
        fun defaults(
            backgroundColor: Color = HilingualTheme.colors.gray200,
            thumbColor: Color = HilingualTheme.colors.white,
            activeContentColor: Color = HilingualTheme.colors.hilingualOrange,
            inactiveContentColor: Color = HilingualTheme.colors.gray500,
        ): SegmentedButtonColors = SegmentedButtonColors(
            backgroundColor = backgroundColor,
            thumbColor = thumbColor,
            activeContentColor = activeContentColor,
            inactiveContentColor = inactiveContentColor,
        )
    }
}

@Preview
@Composable
private fun HilingualSegmentedButtonPreview() {
    HilingualTheme {
        var isSelected by remember { mutableIntStateOf(0) }

        HilingualSegmentedButton(
            options = persistentListOf("교정본", "원본"),
            selectedIndex = isSelected,
            onSelect = { isSelected = it },
        )
    }
}
