package com.hilingual.presentation.feedprofile.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.FeedEmptyCard
import com.hilingual.presentation.feedprofile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.model.FollowingItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun FollowingScreen(
    followings: ImmutableList<FollowingItemModel>,
    onFollowingItemClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (followings.isEmpty()) {
        FeedEmptyCard(
            type = FeedEmptyCardType.NO_FOLLOWING
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = followings,
                key = { it.userId }
            ) { follower ->
                UserActionItem(
                    userId = follower.userId,
                    profileUrl = follower.profileImgUrl,
                    nickname = follower.nickname,
                    isFilled = follower.isFollowing,
                    buttonText = when {
                        follower.isFollowing -> "팔로잉"
                        follower.isFollowed -> "맞팔로우"
                        else -> "팔로우"
                    },
                    onProfileClick = onFollowingItemClick,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "FollowerScreen - Empty")
@Composable
internal fun FollowingScreenPreviewEmpty() {
    HilingualTheme {
        FollowingScreen(
            followings = persistentListOf(),
            onFollowingItemClick = { userId -> println("Preview: Follower item $userId clicked") },
            onButtonClick = { userId -> println("Preview: Button for $userId clicked") },
            modifier = Modifier.padding(16.dp)
        )
    }
}
