package com.hilingual.presentation.feedprofile.tab

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.FeedContent
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.model.DiaryItem
import com.hilingual.presentation.feedprofile.model.LikeDiaryItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DiaryListScreen(
    diaries: ImmutableList<DiaryItem>,
    emptyCardType: FeedEmptyCardType,
    onProfileClick: (Long) -> Unit,
    onContentClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    onMoreClick: (Long) -> Unit,
    onMenuClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (diaries.isEmpty()) {
        FeedEmptyCard(type = emptyCardType)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 48.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(
                items = diaries,
                key = { _, diary -> diary.diaryId }
            ) { index, diary ->
                with(diary) {
                    val streak = (this as? LikeDiaryItemModel)?.streak
                    val userId = (this as? LikeDiaryItemModel)?.userId ?: 0L

                    FeedContent(
                        profileUrl = profileImageUrl,
                        onProfileClick = { onProfileClick(userId) },
                        nickname = nickname,
                        streak = streak,
                        sharedDateInMinutes = sharedDate,
                        onMenuClick = { onMenuClick(diaryId) },
                        content = originalText,
                        onContentClick = { onContentClick(diaryId) },
                        imageUrl = diaryImgUrl,
                        likeCount = likeCount,
                        isLiked = isLiked,
                        onLikeClick = { onLikeClick(diaryId) },
                        onMoreClick = { onMoreClick(diaryId) }
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
