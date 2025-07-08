package com.hilingual.presentation.voca.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.tag.WordPhraseTypeTag
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun VocaDialog(
    onDismiss: () -> Unit,
    phrase: String,
    phraseType: List<String>,
    explanation: String,
    createdAt: Date,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties()
) {
    val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
    val createdAtText = dateFormat.format(createdAt)

    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = HilingualTheme.colors.white,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 28.dp, horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
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
                            R.drawable.ic_save_36_and_filled
                        } else {
                            R.drawable.ic_save_36_and_empty
                        }
                    ),
                    contentDescription = null,
                    modifier = modifier.noRippleClickable(onClick = onBookmarkClick),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = modifier.height(80.dp))
            Text(
                text = "$createdAtText 일기에서 저장됨",
                style = HilingualTheme.typography.captionM12,
                color = HilingualTheme.colors.gray400,
                modifier = modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
private fun VocaDialogPreview() {
    HilingualTheme {
        var isBookmarked by remember { mutableStateOf(true) }
        val previewDate = Date(125, 5, 12)

        VocaDialog(
            onDismiss = {},
            phrase = "end up ~ing",
            phraseType = listOf("동사", "숙어"),
            explanation = "결국 ~하게 되다",
            createdAt = previewDate,
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }
}
