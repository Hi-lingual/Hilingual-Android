package com.hilingual.core.designsystem.component.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.util.formatSharedDate
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.ErrorImageSize
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun FeedContent(
    profileUrl: String,
    onProfileClick: () -> Unit,
    nickname: String,
    streak: Int,
    sharedDateInMinutes: Long,
    onMenuClick: () -> Unit,
    content: String,
    onContentClick: () -> Unit,
    imageUrl: String?,
    diaryId: Long,
    likeCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .padding(vertical = 20.dp)
    ) {
        NetworkImage(
            imageUrl = profileUrl,
            shape = CircleShape,
            modifier = Modifier
                .size(42.dp)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                )
                .noRippleClickable(onClick = onProfileClick)
        )
        Column {
            FeedHeader(
                nickname = nickname,
                streak = streak,
                sharedDateInMinutes = sharedDateInMinutes,
                onMenuClick = onMenuClick
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = content,
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.black,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.noRippleClickable(onClick = onContentClick)
            )

            if (imageUrl != null) {
                NetworkImage(
                    imageUrl = imageUrl,
                    shape = RoundedCornerShape(8.dp),
                    errorImageSize = ErrorImageSize.LARGE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 0.6f)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FeedFooter(
                diaryId = diaryId,
                likeCount = likeCount,
                isLiked = isLiked,
                onLikeClick = onLikeClick,
                onMoreClick = onMoreClick
            )
        }
    }
}

@Composable
private fun FeedHeader(
    nickname: String,
    streak: Int,
    sharedDateInMinutes: Long,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(sharedDateInMinutes) { formatSharedDate(sharedDateInMinutes) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        Text(
            text = nickname,
            style = HilingualTheme.typography.headB16,
            color = HilingualTheme.colors.gray850
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire_16),
            contentDescription = null,
            tint = HilingualTheme.colors.hilingualOrange,
            modifier = Modifier
                .padding(start = 4.dp, end = 1.dp)
                .size(16.dp)
        )

        Text(
            text = streak.toString(),
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.hilingualOrange,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = formattedDate,
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_more_24),
            contentDescription = null,
            tint = HilingualTheme.colors.gray400,
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onMenuClick)
        )
    }
}

@Composable
private fun FeedFooter(
    diaryId: Long,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    likeCount: Int,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (isLiked) R.drawable.ic_like_24 else R.drawable.ic_unliked_24
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onLikeClick)
        )

        Text(
            text = likeCount.toString(),
            style = HilingualTheme.typography.bodySB14,
            color = HilingualTheme.colors.black,
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f)
        )

        Text(
            text = "상세보기",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier.noRippleClickable(onClick = onMoreClick)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right_16),
            contentDescription = null,
            tint = HilingualTheme.colors.gray400,
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable(onClick = onMoreClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedHeaderPreview() {
    HilingualTheme {
        FeedHeader(
            nickname = "HilingualUser",
            streak = 10,
            sharedDateInMinutes = 6000,
            onMenuClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedFooterPreview() {
    HilingualTheme {
        var isLikedPreview by remember { mutableStateOf(true) }
        var likeCountPreview by remember { mutableStateOf(6) }

        FeedFooter(
            likeCount = likeCountPreview,
            isLiked = isLikedPreview,
            onLikeClick = {
                isLikedPreview = !isLikedPreview
                if (isLikedPreview) {
                    likeCountPreview++
                } else {
                    likeCountPreview--
                }
            },
            onMoreClick = {},
            diaryId = 0L
        )
    }
}

@Preview()
@Composable
private fun FeedContentPreviewWithImage() {
    HilingualTheme {
        var isLiked by remember { mutableStateOf(false) }
        var likeCount by remember { mutableStateOf(25) }

        FeedContent(
            profileUrl = "https://picsum.photos/id/237/200/200",
            nickname = "HilingualDev",
            streak = 7,
            sharedDateInMinutes = 3,
            content = "Today was a busy but fulfilling day. I spent the morning working on my project and finally solved a problem that had been bothering me for days. " +
                "In the afternoon, I met a friend for coffee and we talked about our future plans.\n" +
                "The weather was warm and sunny, which made the walk back home really pleasant.\n" +
                "I feel tired now, but also proud of how I spent my day.",
            imageUrl = "https://picsum.photos/id/1060/800/600",
            likeCount = likeCount,
            isLiked = isLiked,
            onProfileClick = { /*프로필 클릭 로직*/ },
            onContentClick = { /* 상세보기 클릭 로직 */ },
            onMenuClick = { /* 메뉴 클릭 로직 */ },
            onLikeClick = {
                isLiked = !isLiked
                if (isLiked) likeCount++ else likeCount--
            },
            onMoreClick = { /* 상세보기 클릭 로직 */ },
            diaryId = 0L
        )
    }
}

@Preview()
@Composable
private fun FeedContentPreviewNoImage() {
    HilingualTheme {
        var isLiked by remember { mutableStateOf(true) }
        var likeCount by remember { mutableStateOf(102) }

        FeedContent(
            profileUrl = "",
            nickname = "User123",
            streak = 32,
            sharedDateInMinutes = 24,
            content = "Today was a busy but fulfilling day.\n" +
                "I spent the morning working on my project and finally solved a problem that had been bothering me for days.\n" +
                "In the afternoon, I met a friend for coffee and we talked about our future plans.\n" +
                "The weather was warm and sunny, which made the walk back home really pleasant.\n" +
                "I feel tired now, but also proud of how I spent my day.",
            imageUrl = null,
            likeCount = likeCount,
            isLiked = isLiked,
            onProfileClick = { /*프로필 클릭 로직*/ },
            onContentClick = { /* 상세보기 클릭 로직 */ },
            onMenuClick = { /* 메뉴 클릭 로직 */ },
            onLikeClick = {
                isLiked = !isLiked
                if (isLiked) likeCount++ else likeCount--
            },
            onMoreClick = { /* 상세보기 클릭 로직 */ },
            diaryId = 0L
        )
    }
}
