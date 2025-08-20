package com.hilingual.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.FeedContent
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feed.model.FeedPreviewUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun FeedTabScreen(
    listState: LazyListState,
    feedList: PersistentList<FeedPreviewUiModel>,
    onProfileClick: (Long) -> Unit,
    onMenuClick: (Long) -> Unit,
    onContentClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 48.dp),
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(
            items = feedList,
            key = { _, feed -> feed.diaryId }
        ) { index, feed ->
            with(feed) {
                FeedContent(
                    profileUrl = profileUrl,
                    onProfileClick = { onProfileClick(userId) },
                    nickname = nickname,
                    streak = streak,
                    sharedDateInMinutes = sharedDateInMinutes,
                    onMenuClick = { onMenuClick(diaryId) },
                    content = content,
                    onContentClick = { onContentClick(diaryId) },
                    imageUrl = imageUrl,
                    diaryId = diaryId,
                    likeCount = likeCount,
                    isLiked = isLiked,
                    onLikeClick = { onLikeClick(diaryId) },
                    onMoreClick = onMoreClick,
                )
            }

            if (index != feedList.lastIndex) HorizontalDivider(
                color = HilingualTheme.colors.gray100,
                thickness = 1.dp
            )
        }
    }
}

@Preview(showBackground = true, name = "Feed List Preview")
@Composable
fun FeedPreviewListScreenPreview() {
    var sampleFeedList by remember {
        mutableStateOf(
            persistentListOf(
                FeedPreviewUiModel(
                    userId = 1,
                    profileUrl = "",
                    nickname = "TravelExplorer",
                    streak = 15,
                    sharedDateInMinutes = 5L,
                    content = "Just enjoyed a beautiful sunset over the mountains! #travel #nature",
                    imageUrl = "",
                    diaryId = 1,
                    likeCount = 120,
                    isLiked = false
                ),
                FeedPreviewUiModel(
                    userId = 2,
                    profileUrl = "",
                    nickname = "FoodieCoder",
                    streak = 3,
                    sharedDateInMinutes = 60L * 2,
                    content = "Trying out a new recipe tonight. It's a bit spicy but delicious! 🌶️",
                    imageUrl = null,
                    diaryId = 2,
                    likeCount = 75,
                    isLiked = true
                ),
                FeedPreviewUiModel(
                    userId = 3,
                    profileUrl = "",
                    nickname = "BookwormReader",
                    streak = 99,
                    sharedDateInMinutes = 60L * 24 * 3,
                    content = "Finished an amazing novel. Highly recommend it to anyone who loves a good mystery. " +
                            "The plot twists were incredible, and the characters were so well-developed. " +
                            "I couldn't put it down until I reached the very last page!",
                    imageUrl = "",
                    diaryId = 3,
                    likeCount = 210,
                    isLiked = false
                )
            )
        )
    }

    HilingualTheme {
        FeedTabScreen(
            listState = rememberLazyListState(),
            feedList = sampleFeedList,
            onProfileClick = { },
            onMenuClick = { },
            onContentClick = { },
            onLikeClick = { },
            onMoreClick = { },
            modifier = Modifier.fillMaxSize() // 프리뷰 화면을 채우도록 Modifier 추가
        )
    }
}