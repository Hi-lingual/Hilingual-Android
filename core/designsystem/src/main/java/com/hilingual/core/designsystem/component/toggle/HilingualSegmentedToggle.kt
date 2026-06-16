package com.hilingual.core.designsystem.component.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

// 색상 토큰 (이미지 기준)
private val TrackColor = Color(0xFFE3E3E3)      // 바깥 트랙(연한 회색)
private val ThumbColor = Color(0xFFFFFFFF)      // 선택된 칸(흰색)
private val SelectedText = Color(0xFFF26B2A)    // 주황 텍스트
private val UnselectedText = Color(0xFF8A8A8A)  // 회색 텍스트
private val ThumbWidth = 42.dp

@Composable
fun HilingualSegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 3.dp,
) {
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.8f,   // 살짝 통통 튀는 느낌
            stiffness = 380f,
        ),
        label = "thumbOffset",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(TrackColor)
            .padding(padding),
    ) {
        val count = options.size

        // 움직이는 흰색 thumb
        Box(
            modifier = Modifier
                .width(ThumbWidth)
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
                .shadow(4.dp, RoundedCornerShape(percent = 50))
                .clip(RoundedCornerShape(percent = 50))
                .background(ThumbColor),
        )

        // 텍스트 라벨 (thumb 위에 균등 분할로 올림)
        Row(Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSelect(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.width(40.dp),
                        color = if (selected) SelectedText else UnselectedText,
                        style = if (selected) HilingualTheme.typography.bodyM14 else HilingualTheme.typography.bodyR14,
                    )
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
            options = listOf("교정본", "원본"),
            selectedIndex = isSelected,
            onSelect = { isSelected = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        )
    }
}
