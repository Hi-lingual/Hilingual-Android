package com.hilingual.presentation.feed.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.feed.UserActionItem
import com.hilingual.data.feed.model.FollowState
import com.hilingual.presentation.feed.component.FeedSearchHeader
import com.hilingual.presentation.feed.component.SearchEmptyCard
import com.hilingual.presentation.feed.model.UserSearchUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun FeedSearchRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit,
    viewModel: FeedSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState.searchWord.isNotBlank()) {
            viewModel.searchUser()
        }
    }

    FeedSearchScreen(
        paddingValues = paddingValues,
        onBackClick = navigateUp,
        searchWord = uiState.searchWord,
        onSearchWordClearClick = viewModel::clearSearchWord,
        onSearchWordChanged = viewModel::updateSearchWord,
        onSearchDone = viewModel::searchUser,
        searchResultUserList = uiState.searchResultUserList,
        onProfileClick = navigateToFeedProfile,
        onFollowActionClick = viewModel::updateFollowingState
    )
}

@Composable
private fun FeedSearchScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    searchWord: String,
    onSearchWordClearClick: () -> Unit,
    onSearchWordChanged: (String) -> Unit,
    onSearchDone: () -> Unit,
    searchResultUserList: UiState<ImmutableList<UserSearchUiModel>>,
    onProfileClick: (Long) -> Unit,
    onFollowActionClick: (Long, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FeedSearchHeader(
            searchWord = { searchWord },
            onSearchWordChanged = onSearchWordChanged,
            onClearClick = onSearchWordClearClick,
            onBackClick = onBackClick,
            onSearchAction = onSearchDone
        )

        when (searchResultUserList) {
            is UiState.Loading -> HilingualLoadingIndicator()

            is UiState.Success -> {
                with(searchResultUserList.data) {
                    if (isEmpty()) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Spacer(Modifier.weight(14f))
                            SearchEmptyCard()
                            Spacer(Modifier.weight(34f))
                        }
                    } else {
                        LazyColumn {
                            items(
                                items = searchResultUserList.data,
                                key = ({ user -> user.userId })
                            ) {
                                with(it) {
                                    UserActionItem(
                                        userId = userId,
                                        profileUrl = profileUrl,
                                        nickname = nickname,
                                        isFilled = !followState.isFollowing,
                                        buttonText = followState.label,
                                        onProfileClick = onProfileClick,
                                        onButtonClick = { onFollowActionClick(userId, followState.isFollowing) },
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedSearchScreenPreview() {
    HilingualTheme {
        var searchWord by remember { mutableStateOf("") }

        FeedSearchScreen(
            paddingValues = PaddingValues(),
            onBackClick = {},
            searchWord = searchWord,
            onSearchWordClearClick = { searchWord = "" },
            onSearchWordChanged = { searchWord = it },
            onSearchDone = {},
            onFollowActionClick = { _, _ -> },
            onProfileClick = {},
            searchResultUserList = UiState.Success(
                persistentListOf(
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
                        nickname = "Daljyeong",
                        profileUrl = "",
                        followState = FollowState.NONE
                    ),
                    UserSearchUiModel(
                        userId = 4L,
                        nickname = "Makers",
                        profileUrl = "",
                        followState = FollowState.ONLY_FOLLOWED
                    )
                )
            )
        )
    }
}
