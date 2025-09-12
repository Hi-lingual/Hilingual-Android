package com.hilingual.presentation.feedprofile.follow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.core.designsystem.component.pulltorefresh.HilingualPullToRefreshBox
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FollowScreen(
    follows: ImmutableList<FollowItemModel>,
    emptyCardType: FeedEmptyCardType,
    isRefreshing: Boolean,
    listState: LazyListState,
    onRefresh: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualPullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 48.dp),
            verticalArrangement = if (follows.isEmpty()) Arrangement.Center else Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.white)
                .padding(horizontal = 16.dp)
        ) {
            if (follows.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxSize()
                    ) {
                        Spacer(Modifier.weight(1f))
                        FeedEmptyCard(type = emptyCardType)
                        Spacer(Modifier.weight(2f))
                    }
                }
            } else {
                items(
                    items = follows,
                    key = { it.userId }
                ) { follow ->
                    with(follow) {
                        UserActionItem(
                            userId = userId,
                            profileUrl = profileImgUrl,
                            nickname = nickname,
                            isFilled = !followState.isFollowing,
                            buttonText = followState.label,
                            onProfileClick = onProfileClick,
                            onButtonClick = { onActionButtonClick(userId, followState.isFollowing) }
                        )
                    }
                }
            }
        }
    }
}
