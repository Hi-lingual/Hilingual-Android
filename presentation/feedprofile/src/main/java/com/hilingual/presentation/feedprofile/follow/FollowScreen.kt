package com.hilingual.presentation.feedprofile.follow

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.FollowState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun FollowScreen(
    follows: ImmutableList<FollowItemModel>,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (follows.isEmpty()) {
        FeedEmptyCard(
            type = FeedEmptyCardType.NO_FOLLOWER,
            modifier = modifier.fillMaxWidth()
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
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
