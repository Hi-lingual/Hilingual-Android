package com.hilingual.core.designsystem.component.datapicker

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.fadingEdge
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.black
import com.hilingual.core.designsystem.theme.gray200
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HilingualBasicPicker(
    items: ImmutableList<String>,
    startIndex: Int,
    onSelectedItemChanged: (String) -> Unit,
    itemContentPadding: PaddingValues,
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    isInfinity: Boolean = true
) {
    val density = LocalDensity.current
    val visibleItemsMiddle = visibleItemsCount / 2

    val adjustedItems = if (!isInfinity) {
        listOf(null) + items + listOf(null)
    } else {
        items
    }

    val listScrollCount = if (isInfinity) Int.MAX_VALUE else adjustedItems.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = if (isInfinity) {
        listScrollMiddle - listScrollMiddle % adjustedItems.size - visibleItemsMiddle + startIndex
    } else {
        startIndex + 1
    }

    var currentCenterIndex by remember(visibleItemsMiddle) { mutableIntStateOf(listStartIndex + visibleItemsMiddle) }

    fun getItem(index: Int) = adjustedItems[index % adjustedItems.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightDp = with(density) {
        HilingualTheme.typography.bodySB14.fontSize.toDp() +
                itemContentPadding.calculateTopPadding() +
                itemContentPadding.calculateBottomPadding()
    }

    val itemHeightPx = with(density) { itemHeightDp.roundToPx() }

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to gray200,
            0.5f to black,
            1f to gray200
        )
    }

    // 중앙 아이템 변경 감지
    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                currentCenterIndex = calculateCenterIndex(
                    firstVisibleItemIndex = index,
                    firstVisibleItemScrollOffset = offset,
                    itemHeightPx = itemHeightPx,
                    visibleItemsMiddle = visibleItemsMiddle
                )
                val selectedItem = getItem(currentCenterIndex)
                if (selectedItem != null) {
                    onSelectedItemChanged(selectedItem)
                }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .height(itemHeightDp * visibleItemsCount + itemSpacing * (visibleItemsCount - 1))
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(
                listScrollCount,
                key = { it }
            ) { index ->
                val currentItemText = getItem(index)?.toString().orEmpty()
                val isSelected = index == currentCenterIndex

                val textColor = remember (isSelected) {
                    if (isSelected) black
                    else gray200
                }

                Text(
                    text = currentItemText,
                    maxLines = 1,
                    color = textColor,
                    style = HilingualTheme.typography.headSB20,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(itemHeightDp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .fillMaxWidth()
                )
            }
        }

        // 중앙 가이드 라인
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeightDp)
        ) {
            HorizontalDivider(
                color = HilingualTheme.colors.black,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            HorizontalDivider(
                color = HilingualTheme.colors.black,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

private fun calculateCenterIndex(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    itemHeightPx: Int,
    visibleItemsMiddle: Int
): Int {
    val correction = if (firstVisibleItemScrollOffset > itemHeightPx / 2) 1 else 0
    return firstVisibleItemIndex + visibleItemsMiddle + correction
}