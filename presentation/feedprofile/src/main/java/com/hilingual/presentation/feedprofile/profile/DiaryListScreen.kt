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
package com.hilingual.presentation.feedprofile.profile

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.item.feed.FeedCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.profile.model.FeedDiaryUIModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DiaryListScreen(
    diaries: ImmutableList<FeedDiaryUIModel>,
    emptyCardType: FeedEmptyCardType,
    onProfileClick: (userId: Long) -> Unit,
    onContentDetailClick: (diaryId: Long) -> Unit,
    onLikeClick: (diaryId: Long, isLiked: Boolean) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 48.dp),
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(horizontal = 16.dp)
    ) {
        if (diaries.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize()
                ) {
                    Spacer(modifier = Modifier.weight(140f))
                    FeedEmptyCard(type = emptyCardType)
                    Spacer(modifier = Modifier.weight(243f))
                }
            }
        } else {
            itemsIndexed(
                items = diaries,
                key = { _, diary -> diary.diaryId }
            ) { index, diary ->
                with(diary) {
                    FeedCard(
                        profileUrl = authorProfileImageUrl,
                        onProfileClick = { onProfileClick(authorUserId) },
                        nickname = authorNickname,
                        streak = authorStreak,
                        sharedDateInMinutes = sharedDate,
                        content = originalText,
                        onContentDetailClick = { onContentDetailClick(diaryId) },
                        imageUrl = diaryImageUrl,
                        likeCount = likeCount,
                        isLiked = isLiked,
                        onLikeClick = { onLikeClick(diaryId, !isLiked) },
                        isMine = isMine,
                        onUnpublishClick = { onUnpublishClick(diaryId) },
                        onReportClick = onReportClick
                    )
                }

                if (index < diaries.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = HilingualTheme.colors.gray100
                    )
                }
            }
        }
    }
}
