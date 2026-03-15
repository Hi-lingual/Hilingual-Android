package com.hilingual.core.ads.native.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
internal fun NativeLineAdContent(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    var chunks by remember(body) { mutableStateOf(listOf(body)) }
    var currentIndex by remember(body) { mutableIntStateOf(0) }

    LaunchedEffect(body) {
        while (isActive) {
            delay(5000L)
            if (chunks.size > 1) {
                currentIndex = (currentIndex + 1) % chunks.size
            }
        }
    }

    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            slideInVertically { it } + fadeIn() togetherWith
                slideOutVertically { -it } + fadeOut()
        },
        label = "NativeAdRolling",
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .heightIn(min = 32.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) { index ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (index == 0) {
                AdMark()
                Text(
                    text = title,
                    color = HilingualTheme.colors.gray850,
                    style = HilingualTheme.typography.bodyM12,
                    maxLines = 1,
                )
            }

            Text(
                text = chunks[index],
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.captionR12,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (result.hasVisualOverflow && chunks.size == index + 1) {
                        val end = result.getLineEnd(0, true)
                        if (end > 0) chunks = chunks + chunks[index].substring(end).trim()
                    }
                },
                modifier = Modifier.weight(1f, fill = false),
            )
        }
    }
}

@Preview
@Composable
private fun NativeLineAdContentPreview() {
    HilingualTheme {
        NativeLineAdContent(
            title = "광고 이름",
            body = "광고 내용입니다. 내용이 길면 5초 후에 롤링됩니다." +
                "아주 아주 아주 아주 아주 길어도 롤링됩니다." +
                "그리고 다시 롤링됩니다. 그래서 설명이 잘리지 않습니다.",
        )
    }
}
