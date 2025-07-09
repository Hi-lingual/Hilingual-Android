package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.tag.WordPhraseTypeTag
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diaryfeedback.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun RecommendExpressionCard(
    phraseType: ImmutableList<String>,
    phrase: String,
    explanation: String,
    reason: String,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
    isMarked: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            ExpressionTopContent(
                tagList = phraseType,
                phrase = phrase,
                explanation = explanation,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = reason,
                style = HilingualTheme.typography.captionM12,
                color = HilingualTheme.colors.gray700
            )
        }
        Icon( // 북마크
            imageVector = ImageVector.vectorResource(
                if (isMarked) R.drawable.ic_save_saved_28 else R.drawable.ic_save_unsaved_28
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(
                onClick = onBookmarkClick
            )
        )
    }
}

@Composable
private fun ExpressionTopContent(
    tagList: ImmutableList<String>,
    phrase: String,
    explanation: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        ExpressionTag(tagList)
        Text(
            text = phrase,
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.black
        )
        Text(
            text = explanation,
            style = HilingualTheme.typography.bodyB14,
            color = HilingualTheme.colors.black
        )
    }
}

@Composable
private fun ExpressionTag(
    tagList: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        tagList.forEach {
            key(it) {
                WordPhraseTypeTag(it)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun RecommendExpressionPreview() {
    HilingualTheme {
        var isBookmarked by remember { mutableStateOf(false) }

        RecommendExpressionCard(
            phraseType = persistentListOf("동사", "숙어"),
            phrase = "come across as",
            explanation = "~처럼 보이다, ~한 인상을 주다",
            reason = "“My life comes across as a disaster.”처럼 자신이나 상황의 ‘이미지’를 묘사할 때 자연스러워요.",
            isMarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }
}