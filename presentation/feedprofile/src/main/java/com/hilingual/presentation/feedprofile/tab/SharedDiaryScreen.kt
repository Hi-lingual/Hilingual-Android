package com.hilingual.presentation.feedprofile.tab

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.FeedContent
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.model.SharedDiaryItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SharedDiaryScreen(
    onProfileClick: () -> Unit,
    sharedDiarys: ImmutableList<SharedDiaryItemModel>,
    onContentClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    onMenuClick: (Long) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (sharedDiarys.isEmpty()) {
        FeedEmptyCard(type = FeedEmptyCardType.NOT_SHARED)
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = sharedDiarys,
                key = { it.diaryId }
            ) { sharedDiary ->
                with(sharedDiary) {
                    FeedContent(
                        profileUrl = profileImageUrl,
                        onProfileClick = onProfileClick,
                        nickname = nickname,
                        sharedDateInMinutes = sharedDate,
                        streak = null,
                        onMenuClick = { onMenuClick(diaryId) },
                        content = originalText,
                        onContentClick = { onContentClick(diaryId) },
                        imageUrl = diaryImgUrl,
                        likeCount = likeCount,
                        isLiked = isLiked,
                        onLikeClick = { onLikeClick(diaryId) },
                        onMoreClick = onMoreClick
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = HilingualTheme.colors.gray100)
            }
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
