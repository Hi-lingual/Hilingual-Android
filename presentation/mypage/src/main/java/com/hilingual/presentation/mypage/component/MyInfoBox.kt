package com.hilingual.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun MyInfoBox(
    modifier: Modifier = Modifier,
    profileUrl: String,
    profileNickname: String,
    onEditBtnClicked: () -> Unit,
    onMyFeedBtnClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(HilingualTheme.colors.white)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                imageUrl = profileUrl,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = profileNickname,
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headB18
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                    .clickable { onEditBtnClicked },
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_pen_24),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(HilingualTheme.colors.black)
                .clickable { onMyFeedBtnClicked }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "나의 피드",
                color = HilingualTheme.colors.white,
                style = HilingualTheme.typography.bodySB14
            )
        }
    }
}

@Preview
@Composable
private fun MyInfoBoxPreview() {
    HilingualTheme {
        MyInfoBox(
            profileUrl = "",
            profileNickname = "내 닉네임",
            onEditBtnClicked = { println("Block Button Clicked in Preview") },
            onMyFeedBtnClicked = {}
        )
    }
}
