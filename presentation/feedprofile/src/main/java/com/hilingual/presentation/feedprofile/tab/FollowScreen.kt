package com.hilingual.presentation.feedprofile.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.presentation.feedprofile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.model.FollowItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun FollowScreen(
    follow: ImmutableList<FollowItemModel>,
    onProfileClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (follow.isEmpty()) {
        FeedEmptyCard(
            type = FeedEmptyCardType.NO_FOLLOWER
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = follow,
                key = { it.userId }
            ) { follower ->
                with(follower) {
                    UserActionItem(
                        userId = userId,
                        profileUrl = profileImgUrl,
                        nickname = nickname,
                        isFilled = isFollowing,
                        buttonText = when {
                            isFollowing -> "팔로잉"
                            isFollowed -> "맞팔로우"
                            else -> "팔로우"
                        },
                        onProfileClick = onProfileClick,
                        onButtonClick = onButtonClick
                    )
                }
            }
        }
    }
}
