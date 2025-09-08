package com.hilingual.presentation.feed.model

import com.hilingual.data.feed.model.FollowState

internal data class UserSearchUiModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val followState: FollowState
)
