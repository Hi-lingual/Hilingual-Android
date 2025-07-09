package com.hilingual.presentation.voca.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.hilingual.presentation.voca.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun VocaModal(
    phrase: String,
    phraseType: ImmutableList<String>,
    explanation: String,
    createdAt: String,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 28.dp, horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                phraseType.forEach { type ->
                    key(type) {
                        WordPhraseTypeTag(phraseType = type)
                    }
                }
            }

            Text(
                text = phrase,
                style = HilingualTheme.typography.bodyM20,
                color = HilingualTheme.colors.black
            )

            Text(
                text = explanation,
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.black
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (isBookmarked) {
                    R.drawable.ic_save_36_filled
                } else {
                    R.drawable.ic_save_36_empty
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .noRippleClickable(onClick = onBookmarkClick),
            tint = Color.Unspecified
        )
        Text(
            text = "$createdAt 일기에서 저장됨",
            style = HilingualTheme.typography.captionM12,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}


@Preview
@Composable
private fun VocaModalPreview() {
    HilingualTheme {
        var isBookmarked by remember { mutableStateOf(true) }

        VocaModal(
            phrase = "end up ~ing",
            phraseType = persistentListOf("동사", "숙어"),
            explanation = "결국 ~하게 되다",
            createdAt = "25.06.12",
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }
}
