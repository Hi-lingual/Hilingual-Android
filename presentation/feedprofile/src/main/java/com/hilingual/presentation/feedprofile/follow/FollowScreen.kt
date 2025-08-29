package com.hilingual.presentation.feedprofile.follow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.FollowState
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun FollowScreen(
    follows: ImmutableList<FollowItemModel>,
    emptyCardType: FeedEmptyCardType,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 48.dp),
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(horizontal = 16.dp)
    ) {
        if (follows.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize()
                ) {
                    Spacer(Modifier.weight(16f))
                    FeedEmptyCard(type = emptyCardType)
                }
            }
        } else {
            items(
                items = follows,
                key = { it.userId }
            ) { follow ->
                with(follow) {
                    val followState = FollowState.getValueByFollowState(
                        isFollowing = isFollowing,
                        isFollowed = isFollowed
                    ) ?: FollowState.NONE

                    UserActionItem(
                        userId = userId,
                        profileUrl = profileImgUrl,
                        nickname = nickname,
                        isFilled = followState.isFollowing,
                        buttonText = followState.actionText,
                        onProfileClick = onProfileClick,
                        onButtonClick = { onActionButtonClick(userId, followState.isFollowing) }
                    )
                }
            }
        }
    }
}
