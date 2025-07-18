package com.hilingual.presentation.home.component.footer

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R

@Composable
internal fun TodayTopic(
    koTopic: String,
    enTopic: String,
    modifier: Modifier = Modifier
) {
    var isKo by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.gray100)
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "오늘의 추천 주제",
                style = HilingualTheme.typography.captionM12,
                color = HilingualTheme.colors.gray500
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_transfer_28),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable { isKo = !isKo }
            )
        }

        Text(
            text = (if (isKo) koTopic else enTopic).let {
                if (it.length > 70) it.take(70) else it
            },
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.gray700,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun TodayTopicPreview() {
    HilingualTheme {
        TodayTopic(
            enTopic = "What surprised you today?",
            koTopic = "오늘 무엇이 당신을 놀라게 했나요?"
        )
    }
}
