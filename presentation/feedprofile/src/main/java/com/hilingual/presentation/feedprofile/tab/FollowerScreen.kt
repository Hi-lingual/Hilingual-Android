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
import com.hilingual.presentation.feedprofile.model.FollowerItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun FollowerScreen(
    followers: ImmutableList<FollowerItemModel>,
    onFollowerItemClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (followers.isEmpty()) {
        FeedEmptyCard(
            type = FeedEmptyCardType.NO_FOLLOWER
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = followers,
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
                    onProfileClick = onFollowerItemClick,
                    onButtonClick = onButtonClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "FollowerScreen - Empty")
@Composable
internal fun FollowerScreenPreviewEmpty() {
    HilingualTheme {
        FollowerScreen(
            followers = persistentListOf(),
            onFollowerItemClick = { userId -> println("Preview: Follower item $userId clicked") },
            onButtonClick = { userId -> println("Preview: Button for $userId clicked") },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "FollowerScreen - With Data")
@Composable
internal fun FollowerScreenPreviewWithData() {
    HilingualTheme {
        var sampleFollowers by remember {
            mutableStateOf(
                persistentListOf(
                    FollowerItemModel(
                        1L,
                        "https://picsum.photos/id/1005/200/200",
                        "Following User",
                        isFollowing = true,
                        isFollowed = false
                    ),
                    FollowerItemModel(
                        2L,
                        "https://picsum.photos/id/1012/200/200",
                        "Followed By User",
                        isFollowing = false,
                        isFollowed = true
                    ),
                    FollowerItemModel(
                        3L,
                        "https://picsum.photos/id/1025/200/200",
                        "New User",
                        isFollowing = false,
                        isFollowed = false
                    ),
                    FollowerItemModel(
                        4L,
                        "https://picsum.photos/id/1027/200/200",
                        "Mutual Follow",
                        isFollowing = true,
                        isFollowed = true
                    )
                )
            )
        }
        FollowerScreen(
            followers = sampleFollowers,
            onFollowerItemClick = { userId ->
                println("Preview: Follower item $userId clicked")
            },
            onButtonClick = { userId ->
                println("Preview: Button for $userId clicked ($userId)")
                sampleFollowers = sampleFollowers.map { follower ->
                    if (follower.userId == userId) {
                        follower.copy(isFollowing = !follower.isFollowing)
                    } else {
                        follower
                    }
                }.toPersistentList()
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}