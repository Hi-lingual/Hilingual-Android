package com.hilingual.core.designsystem.component.datapicker

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
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
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    itemPadding: PaddingValues = PaddingValues(8.dp),
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

    fun getItem(index: Int) = adjustedItems[index % adjustedItems.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeight = with(density) {
        HilingualTheme.typography.bodySB14.fontSize.toDp() +
                itemPadding.calculateTopPadding() +
                itemPadding.calculateBottomPadding()
    }

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to gray200,
            0.5f to black,
            1f to gray200
        )
    }

    // 스크롤 멈췄을 때 현재 아이템 계산
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                val centerIndex = index + visibleItemsMiddle
                val selectedItem = getItem(centerIndex)
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
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .height(itemHeight * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(
                listScrollCount,
                key = { it }
            ) { index ->
                val currentItemText = getItem(index)?.toString().orEmpty()

                Text(
                    text = currentItemText,
                    maxLines = 1,
                    color = HilingualTheme.colors.black,
                    style = HilingualTheme.typography.headSB20,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(itemHeight)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
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