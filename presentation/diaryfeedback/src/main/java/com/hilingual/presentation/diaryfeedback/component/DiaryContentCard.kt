package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun DiaryContentCard(
    content: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        if (imageUrl != null) {
            NetworkImage(
                imageUrl = imageUrl,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 0.6f)
            )
        }
        Text(
            text = content,
            style = HilingualTheme.typography.bodyR16,
            color = HilingualTheme.colors.black,
            modifier = Modifier.heightIn(min = 45.dp)
        )
        Text(
            text = "${content.length}/1000",
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray400,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun FeedbackContentPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DiaryContentCard(
                imageUrl = "",
                content = "텍스트",
            )
            DiaryContentCard(
                content = "I want to become a teacher future. Because I like child."
            )
        }
    }
}