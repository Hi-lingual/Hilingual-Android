package com.hilingual.presentation.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun MyInfoBox(
    profileUrl: String,
    profileNickname: String,
    onEditButtonClick: () -> Unit,
    onMyFeedButtonClick: () -> Unit,
    modifier: Modifier = Modifier
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
            val imageModifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                )
                .noRippleClickable(onClick = onEditButtonClick)

            if (profileUrl.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.img_default_image),
                    contentDescription = null,
                    modifier = imageModifier
                )
            } else {
                NetworkImage(
                    imageUrl = profileUrl,
                    modifier = imageModifier
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                modifier = Modifier.noRippleClickable(onClick = onEditButtonClick),
                text = profileNickname,
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headB18
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClickable(onClick = onEditButtonClick)
                    .padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_pen_24),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )
        }

        Text(
            text = "나의 피드",
            color = HilingualTheme.colors.white,
            style = HilingualTheme.typography.bodySB14,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .noRippleClickable(onClick = onMyFeedButtonClick)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(HilingualTheme.colors.black)
                .padding(vertical = 10.dp)
        )
    }
}

@Preview
@Composable
private fun MyInfoBoxPreview() {
    HilingualTheme {
        MyInfoBox(
            profileUrl = "",
            profileNickname = "내 닉네임",
            onEditButtonClick = { println("Block Button Clicked in Preview") },
            onMyFeedButtonClick = {}
        )
    }
}
