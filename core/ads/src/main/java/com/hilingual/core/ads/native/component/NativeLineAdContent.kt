package com.hilingual.core.ads.native.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
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
    var isVisualOverflowed by remember { mutableStateOf(false) }
    var isExpanded by remember(title, body) { mutableStateOf(false) }

    LaunchedEffect(isVisualOverflowed, title, body) {
        if (isVisualOverflowed) {
            while (isActive) {
                delay(5000L)
                isExpanded = !isExpanded
            }
        } else {
            isExpanded = false
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .heightIn(min = 26.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AnimatedVisibility(
            visible = !isExpanded,
            enter = expandHorizontally(animationSpec = tween(500)),
            exit = shrinkHorizontally(animationSpec = tween(500)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                AdMark()
                Text(
                    text = title,
                    color = HilingualTheme.colors.gray850,
                    style = HilingualTheme.typography.bodyM12,
                    maxLines = 1,
                )
            }
        }

        Text(
            text = body,
            color = HilingualTheme.colors.gray850,
            style = HilingualTheme.typography.captionR12,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (!isExpanded) isVisualOverflowed = it.hasVisualOverflow
            },
        )
    }
}

@Preview
@Composable
private fun NativeLineAdContentPreview() {
    HilingualTheme {
        NativeLineAdContent(
            title = "광고 이름",
            body = "광고 내용입니다. 내용이 길면 5초 후에 롤링됩니다." +
                "그리고 다시 롤링됩니다. 부들부들. 배고파. 그만할래. 살려줘. 제발",
        )
    }
}
