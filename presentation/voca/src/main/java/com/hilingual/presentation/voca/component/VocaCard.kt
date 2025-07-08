package com.hilingual.presentation.voca.component

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.tag.WordPhraseTypeTag
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.R

@Composable
internal fun VocaCard(
    phrase: String,
    phraseType: List<String>,
    onCardClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .noRippleClickable(onClick = onCardClick)
            .fillMaxWidth()
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),

            ) {
                phraseType.forEach { type ->
                    WordPhraseTypeTag(phraseType = type)
                }
            }
            Text(
                text = phrase,
                style = HilingualTheme.typography.headM18,
                color = HilingualTheme.colors.black
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (isBookmarked) {
                    R.drawable.ic_save_28_and_filled
                } else {
                    R.drawable.ic_save_28_and_empty
                }
            ),
            contentDescription = null,
            modifier = modifier.noRippleClickable(onClick = onBookmarkClick),
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
fun VocaCardPreview() {
    HilingualTheme {
        var isBookmarked by remember { mutableStateOf(true) }
        VocaCard(
            phrase = "run late",
            phraseType = listOf("동사", "숙어"),
            onCardClick = {},
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }
}

