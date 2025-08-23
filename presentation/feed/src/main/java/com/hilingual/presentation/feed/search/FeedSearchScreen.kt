package com.hilingual.presentation.feed.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
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
import com.hilingual.presentation.feed.component.FeedSearchHeader
import com.hilingual.presentation.feed.model.FollowState
import com.hilingual.presentation.feed.model.UserSearchUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun FeedSearchScreen(
    paddingValues: PaddingValues,
    userList: ImmutableList<UserSearchUiModel>
) {
    var searchText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        stickyHeader {
            FeedSearchHeader(
                searchText = { searchText },
                onSearchTextChanged = { searchText = it },
                onClearClick = {},
                onBackClick = {}
            )
        }

        items(userList) {
            UserActionItem(
                userId = it.userId,
                profileUrl = it.profileUrl,
                nickname = it.nickname,
                isFilled = it.followState.isFollowing,
                buttonText = it.followState.actionText,
                onProfileClick = {},
                onButtonClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedSearchScreenPreview() {
    HilingualTheme {
        FeedSearchScreen(
            paddingValues = PaddingValues(),
            userList = persistentListOf(
                UserSearchUiModel(
                    userId = 1L,
                    nickname = "작나",
                    profileUrl = "",
                    followState = FollowState.ONLY_FOLLOWING
                ),
                UserSearchUiModel(
                    userId = 2L,
                    nickname = "큰나",
                    profileUrl = "",
                    followState = FollowState.MUTUAL_FOLLOW
                ),
                UserSearchUiModel(
                    userId = 3L,
                    nickname = "Daljeong",
                    profileUrl = "",
                    followState = FollowState.NONE
                ),
                UserSearchUiModel(
                    userId = 4L,
                    nickname = "Makers",
                    profileUrl = "",
                    followState = FollowState.ONLY_FOLLOWED
                ),
            )
        )
    }
}