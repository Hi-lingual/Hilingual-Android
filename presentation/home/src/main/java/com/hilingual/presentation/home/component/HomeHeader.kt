package com.hilingual.presentation.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun HomeHeader(
    imageUrl: String,
    nickname: String,
    totalDiaries: Int,
    streak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileImage(imageUrl)

        Column(modifier = Modifier.weight(1f)) {
            ProfileName(nickname)
            UserStatsRow(totalDiaries, streak)
        }
    }
}

@Composable
private fun ProfileImage(imageUrl: String) {
    Image(
        painter = painterResource(DesignSystemR.drawable.img_default_image),
        contentDescription = null,
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
    )
    // TODO: 앱잼 이후 서버 드리븐으로 변경 by.민재
    //    NetworkImage(
    //        imageUrl = imageUrl,
    //        modifier = Modifier.size(46.dp)
    //    )
}

@Composable
private fun ProfileName(name: String) {
    Text(
        text = name,
        style = HilingualTheme.typography.headB18,
        color = HilingualTheme.colors.white,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun UserStatsRow(totalDiaries: Int, streak: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DiaryCountStat(totalDiaries)

        StatSeparator()

        StreakStat(streak)
    }
}

@Composable
private fun DiaryCountStat(count: Int) {
    StatItem(
        icon = R.drawable.ic_bubble_16,
        text = if (count > 0) "총 ${count}편" else "일기를 작성해보세요!"
    )
}

@Composable
private fun StreakStat(days: Int) {
    StatItem(
        icon = R.drawable.ic_fire_16,
        text = "${days}일 연속 작성 중"
    )
}

@Composable
private fun StatItem(@DrawableRes icon: Int, text: String) {
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = null,
        tint = Color.Unspecified
    )

    Text(
        text = text,
        style = HilingualTheme.typography.captionM12,
        color = HilingualTheme.colors.white,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun StatSeparator() {
    Text(
        text = "·",
        style = HilingualTheme.typography.captionM12,
        color = HilingualTheme.colors.gray100
    )
}

@Preview
@Composable
private fun HomeHeaderPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.background(HilingualTheme.colors.black)
        ) {
            HomeHeader(
                imageUrl = "",
                nickname = "닉네임1",
                totalDiaries = 10,
                streak = 5
            )
            HomeHeader(
                imageUrl = "",
                nickname = "닉네임2",
                totalDiaries = 0,
                streak = 0
            )
        }
    }
}
