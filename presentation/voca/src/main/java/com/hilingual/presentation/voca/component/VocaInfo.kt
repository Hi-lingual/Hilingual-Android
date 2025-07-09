package com.hilingual.presentation.voca.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.R

enum class WordSortType(val text: String) {
    Latest(text = "최신순"),
    AtoZ(text = "A-Z 순")
}

@Composable
fun VocaInfo(
    wordCount: Int?,
    sortType: WordSortType,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "총 ${wordCount ?: 0}개",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray500
        )
        Row(
            modifier = Modifier.noRippleClickable(onClick = onSortClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_list_24),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = sortType.text,
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VocaInfoPreview() {
    HilingualTheme {
        VocaInfo(wordCount = 98, sortType = WordSortType.Latest, onSortClick = {})
    }
}
