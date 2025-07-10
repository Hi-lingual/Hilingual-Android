package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun RecommendedTopicDropdown(
    enTopic: String,
    koTopic: String,
    modifier: Modifier = Modifier
) {
    var isKo by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }  // 확장 여부

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.gray100)
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // '오늘의 추천 주제 참고하기' 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "오늘의 추천 주제 참고하기",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray700
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleY = if (isExpanded) -1f else 1f
                    }
                    .noRippleClickable { isExpanded = !isExpanded },
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down_20),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )
        }

        // 드롭다운 활성화되었을 때 보이는 추천 주제 영역
        if (isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(HilingualTheme.colors.white)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isKo) {
                        koTopic
                    } else {
                        enTopic
                    },
                    style = HilingualTheme.typography.bodySB16,
                    color = HilingualTheme.colors.gray700
                )
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable { isKo = !isKo },
                    imageVector = ImageVector.vectorResource(R.drawable.ic_change_20),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray300
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecommendedTopicDropdownPreview() {
    HilingualTheme {
        RecommendedTopicDropdown(
            enTopic = "What surprised you today?",
            koTopic = "오늘 무엇이 당신을 놀라게 했나요?"
        )
    }
}