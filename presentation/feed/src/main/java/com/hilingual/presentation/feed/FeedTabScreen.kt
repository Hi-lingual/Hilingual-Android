/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.pulltorefresh.HilingualPullToRefreshBox
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.feed.FeedCard
import com.hilingual.presentation.feed.component.FeedEmptyCard
import com.hilingual.presentation.feed.component.FeedEmptyCardType
import com.hilingual.presentation.feed.model.FeedItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedTabScreen(
    listState: LazyListState,
    feedListState: UiState<ImmutableList<FeedItemUiModel>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasFollowing: Boolean = true
) {
    when (val state = feedListState) {
        is UiState.Loading -> HilingualLoadingIndicator()
        is UiState.Success -> {
            val feedList = state.data

            HilingualPullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 48.dp),
                    modifier = Modifier
                        .background(HilingualTheme.colors.white)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    if (feedList.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillParentMaxSize()
                            ) {
                                Spacer(Modifier.weight(16f))
                                FeedEmptyCard(
                                    type = if (hasFollowing) FeedEmptyCardType.NOT_FEED else FeedEmptyCardType.NOT_FOLLOWING
                                )
                                Spacer(Modifier.weight(30f))
                            }
                        }
                    } else {
                        itemsIndexed(
                            items = feedList,
                            key = { _, feed -> feed.diaryId }
                        ) { index, feed ->
                            with(feed) {
                                FeedCard(
                                    profileUrl = profileUrl,
                                    onProfileClick = { onProfileClick(userId) },
                                    nickname = nickname,
                                    streak = streak,
                                    sharedDateInMinutes = sharedDateInMinutes,
                                    content = content,
                                    onContentDetailClick = { onContentDetailClick(diaryId) },
                                    imageUrl = imageUrl,
                                    likeCount = likeCount,
                                    isLiked = isLiked,
                                    onLikeClick = { onLikeClick(diaryId, !isLiked) },
                                    isMine = isMine,
                                    onUnpublishClick = { onUnpublishClick(diaryId) },
                                    onReportClick = onReportClick
                                )
                            }

                            if (index != feedList.lastIndex) {
                                HorizontalDivider(
                                    color = HilingualTheme.colors.gray100,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedTabScreenPreview() {
    var sampleFeedList by remember {
        mutableStateOf(
            persistentListOf(
                FeedItemUiModel(
                    userId = 1,
                    profileUrl = "",
                    nickname = "TravelExplorer",
                    streak = 15,
                    sharedDateInMinutes = 5L,
                    content = "Just enjoyed a beautiful sunset over the mountains! #travel #nature",
                    imageUrl = "",
                    diaryId = 1,
                    likeCount = 120,
                    isLiked = false,
                    isMine = true
                ),
                FeedItemUiModel(
                    userId = 2,
                    profileUrl = "",
                    nickname = "FoodieCoder",
                    streak = 3,
                    sharedDateInMinutes = 60L * 2,
                    content = "Trying out a new recipe tonight. It's a bit spicy but delicious! 🌶️",
                    imageUrl = null,
                    diaryId = 2,
                    likeCount = 75,
                    isLiked = true,
                    isMine = false
                )
            )
        )
    }

    HilingualTheme {
        FeedTabScreen(
            listState = rememberLazyListState(),
            feedListState = UiState.Success(sampleFeedList),
            isRefreshing = false,
            onRefresh = { },
            onProfileClick = { },
            onContentDetailClick = { },
            onLikeClick = { _, _ -> },
            onUnpublishClick = { },
            onReportClick = { },
            hasFollowing = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}
