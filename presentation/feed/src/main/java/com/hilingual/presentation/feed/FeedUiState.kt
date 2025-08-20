package com.hilingual.presentation.feed

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.feed.model.FeedPreviewUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class FeedUiState(
    val recommendFeedList: ImmutableList<FeedPreviewUiModel> = persistentListOf(),
    val followingFeedList: ImmutableList<FeedPreviewUiModel> = persistentListOf(),
    val hasFollowing: Boolean = true
)
