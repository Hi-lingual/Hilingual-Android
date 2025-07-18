package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryPreviewCard(
    diaryText: String,
    diaryId: Long,
    onClick: (diaryId: Long) -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { onClick(diaryId) })
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VerticalDivider(
            modifier = Modifier.fillMaxHeight().heightIn(min = 74.dp),
            thickness = 3.dp,
            color = HilingualTheme.colors.hilingualBlack
        )

        Text(
            text = diaryText,
            style = HilingualTheme.typography.bodyM16,
            color = HilingualTheme.colors.black,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (imageUrl != null) {
            NetworkImage(
                imageUrl = imageUrl,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .size(74.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryPreviewCardPreview() {
    HilingualTheme {
        val text = "This is a sample diary text that will be displayed"
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            DiaryPreviewCard(
                imageUrl = "",
                diaryText = text,
                diaryId = 0,
                onClick = {}
            )

            DiaryPreviewCard(
                imageUrl = null,
                diaryText = text,
                diaryId = 0,
                onClick = {}
            )
        }
    }
}
