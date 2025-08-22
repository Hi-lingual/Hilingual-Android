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
import com.hilingual.presentation.feedprofile.model.LikeDiaryItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun LikedDiaryScreen(
    likedDiarys: ImmutableList<LikeDiaryItemModel>,
    onProfileClick: () -> Unit,
    onLikeDiaryClick: () -> Unit,
    onLikeClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (likedDiarys.isEmpty()) {
        FeedEmptyCard(type = FeedEmptyCardType.NOT_LIKED)
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = likedDiarys,
                key = { it.diaryId }
            ) {
                likedDiary ->
                FeedContent(
                    profileUrl = likedDiary.profileImageUrl,
                    onProfileClick = onProfileClick,
                    nickname = likedDiary.nickname,
                    streak = likedDiary.streak,
                    sharedDateInMinutes = likedDiary.sharedDate,
                    onMenuClick = onMenuClick,
                    content = likedDiary.originalText,
                    onContentClick = onLikeDiaryClick,
                    imageUrl = likedDiary.diaryImgUrl,
                    diaryId = likedDiary.diaryId,
                    likeCount = likedDiary.likeCount,
                    isLiked = likedDiary.isLiked,
                    onLikeClick = onLikeClick,
                    onMoreClick = onLikeDiaryClick
                )
                HorizontalDivider(thickness = 1.dp, color = HilingualTheme.colors.gray100)
            }
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

    }
}