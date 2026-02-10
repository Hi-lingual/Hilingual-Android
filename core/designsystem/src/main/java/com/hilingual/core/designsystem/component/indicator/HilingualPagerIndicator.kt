package com.hilingual.core.designsystem.component.indicator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.coroutines.delay

@Composable
fun HilingualPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeIndicatorWidth: Dp = 20.dp,
    inactiveIndicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = 8.dp,
    indicatorSpacing: Dp = 8.dp,
    activeIndicatorColor: Color = HilingualTheme.colors.hilingualOrange,
    inactiveIndicatorColor: Color = HilingualTheme.colors.gray200
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(indicatorSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            val animatedWidth by animateDpAsState(
                targetValue = if (isSelected) activeIndicatorWidth else inactiveIndicatorWidth,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                label = "indicatorWidth"
            )

            Box(
                modifier = Modifier
                    .height(indicatorHeight)
                    .width(animatedWidth)
                    .clip(CircleShape)
                    .background(
                        color = if (isSelected) activeIndicatorColor else inactiveIndicatorColor
                    )
            )
        }
    }
}

@Preview
@Composable
private fun HilingualPagerIndicatorPreview() {
    val pageCount = 4
    var currentPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1_000L)
            currentPage = (currentPage + 1) % pageCount
        }
    }

    HilingualTheme {
        HilingualPagerIndicator(
            pageCount = pageCount,
            currentPage = currentPage
        )
    }
}
