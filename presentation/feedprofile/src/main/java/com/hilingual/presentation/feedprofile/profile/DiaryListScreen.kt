package com.hilingual.presentation.feedprofile.profile

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
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.profile.model.DiaryItem
import com.hilingual.presentation.feedprofile.profile.model.LikeDiaryItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DiaryListScreen(
    diaries: ImmutableList<DiaryItem>,
    emptyCardType: FeedEmptyCardType,
    onProfileClick: (userId: Long) -> Unit,
    onContentClick: (diaryId: Long) -> Unit,
    onLikeClick: (diaryId: Long) -> Unit,
    onMoreClick: (diaryId: Long) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    onReportClick: (diaryId: Long) -> Unit,
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
                    val isMine = (this as? LikeDiaryItemModel)?.isMine ?: true

                    FeedContent(
                        profileUrl = profileImageUrl,
                        onProfileClick = { onProfileClick(userId) },
                        nickname = nickname,
                        streak = streak,
                        sharedDateInMinutes = sharedDate,
                        content = originalText,
                        onContentClick = { onContentClick(diaryId) },
                        imageUrl = diaryImageUrl,
                        likeCount = likeCount,
                        isLiked = isLiked,
                        onLikeClick = { onLikeClick(diaryId) },
                        onMoreClick = { onMoreClick(diaryId) },
                        isMine = isMine,
                        onUnpublishClick = { onUnpublishClick(diaryId) },
                        onReportClick = { onReportClick(diaryId) }
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
