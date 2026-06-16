package com.hilingual.core.designsystem.component.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HilingualSegmentedToggle(
    options: ImmutableList<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(3.dp),
    backgroundColor: Color = HilingualTheme.colors.gray200,
    thumbColor: Color = HilingualTheme.colors.white,
    activeContentColor: Color = HilingualTheme.colors.hilingualOrange,
    inactiveContentColor: Color = HilingualTheme.colors.gray500
) {
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 380f,
        ),
        label = "thumbOffset",
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(contentPadding),
    ) {
        val count = options.size

        Box(
            modifier = Modifier
                .matchParentSize()
                .layout { measurable, constraints ->
                    val thumbWidth = constraints.maxWidth / count
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = thumbWidth,
                            maxWidth = thumbWidth,
                        )
                    )
                    layout(constraints.maxWidth, placeable.height) {
                        val x = (animatedIndex * thumbWidth).toInt()
                        placeable.placeRelative(x, 0)
                    }
                }
                .clip(CircleShape)
                .background(thumbColor),
        )

        Row(Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSelect(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.widthIn(min = 40.dp),
                        textAlign = TextAlign.Center,
                        color = if (selected) activeContentColor else inactiveContentColor,
                        style = if (selected) HilingualTheme.typography.bodyM14 else HilingualTheme.typography.bodyR14,
                    )
                }

                if (index != options.lastIndex) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun HilingualSegmentedTogglePreview() {
    HilingualTheme {
        var isSelected by remember { mutableIntStateOf(0) }

        HilingualSegmentedToggle(
            options = persistentListOf("교정본", "원본"),
            selectedIndex = isSelected,
            onSelect = { isSelected = it },
            modifier = Modifier
                .width(133.dp),
        )
    }
}
