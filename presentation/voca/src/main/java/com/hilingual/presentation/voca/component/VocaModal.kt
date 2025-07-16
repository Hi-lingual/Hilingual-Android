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
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.tag.WordPhraseTypeTag
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.R
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun VocaModal(
    phraseId: Long,
    phrase: String,
    phraseType: ImmutableList<String>,
    explanation: String,
    writtenDate: String,
    isBookmarked: Boolean,
    onBookmarkClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMarked by remember { mutableStateOf(isBookmarked) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 40.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
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
                id = if (isMarked) {
                    R.drawable.ic_save_36_filled
                } else {
                    R.drawable.ic_save_36_empty
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .noRippleClickable {
                    isMarked = !isMarked
                    onBookmarkClick(phraseId, isMarked)
                },
            tint = Color.Unspecified
        )

        Text(
            text = "$writtenDate 일기에서 저장됨",
            style = HilingualTheme.typography.captionM12,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
