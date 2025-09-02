package com.hilingual.presentation.feedprofile.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.FeedCard
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
    onContentDetailClick: (diaryId: Long) -> Unit,
    onLikeClick: (diaryId: Long) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    onReportClick: () -> Unit,
    onScrollStateChanged: ((Boolean) -> Unit)? = null,
    isNestedScroll: Boolean = false,
    canParentScroll: Boolean = true,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState.layoutInfo, diaries.size) {
        val layoutInfo = listState.layoutInfo

        if (diaries.isEmpty()) {
            onScrollStateChanged?.invoke(false)
            return@LaunchedEffect
        }

        if (layoutInfo.visibleItemsInfo.isEmpty()) {
            onScrollStateChanged?.invoke(false)
            return@LaunchedEffect
        }

        val canScrollDown = listState.canScrollForward
        val canScrollUp = listState.canScrollBackward
        val needsScroll = canScrollDown || canScrollUp

        onScrollStateChanged?.invoke(needsScroll)
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 48.dp),
        userScrollEnabled = if (isNestedScroll) canParentScroll else true,
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
                    val streak = (this as? LikeDiaryItemModel)?.streak
                    val userId = (this as? LikeDiaryItemModel)?.userId ?: 0L
                    val isMine = (this as? LikeDiaryItemModel)?.isMine ?: true

                    FeedCard(
                        profileUrl = profileImageUrl,
                        onProfileClick = { onProfileClick(userId) },
                        nickname = nickname,
                        streak = streak,
                        sharedDateInMinutes = sharedDate,
                        content = originalText,
                        onContentDetailClick = { onContentDetailClick(diaryId) },
                        imageUrl = diaryImageUrl,
                        likeCount = likeCount,
                        isLiked = isLiked,
                        onLikeClick = { onLikeClick(diaryId) },
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
