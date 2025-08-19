package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

internal enum class FollowListEmptyCardType(
    val text: String
) {
    NO_FOLLOWER(
        text = "아직 팔로워가 없어요."
    ),
    NO_FOLLOWING(
        text = "아직 팔로잉한 유저가 없어요."
    )
}

@Composable
internal fun FollowListEmptyCard(
    type: FollowListEmptyCardType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Image(
            modifier = Modifier
                .size(width = 200.dp, height = 100.dp),
            painter = painterResource(id = DesignSystemR.drawable.img_diary_empty),
            contentDescription = null
        )
        Text(
            text = type.text,
            style = HilingualTheme.typography.headM18,
            color = HilingualTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}

private class FollowListEmptyCardPreviewParameterProvider :
    PreviewParameterProvider<FollowListEmptyCardType> {
    override val values: Sequence<FollowListEmptyCardType>
        get() = FollowListEmptyCardType.entries.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun FollowListEmptyCardPreview(
    @PreviewParameter(FollowListEmptyCardPreviewParameterProvider::class)
    type: FollowListEmptyCardType
) {
    HilingualTheme {
        FollowListEmptyCard(type = type)
    }
}
