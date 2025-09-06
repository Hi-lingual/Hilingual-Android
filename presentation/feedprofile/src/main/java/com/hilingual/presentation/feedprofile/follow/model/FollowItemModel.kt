package com.hilingual.presentation.feedprofile.follow.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.user.model.FollowState

@Immutable
data class FollowItemModel(
    val userId: Long,
    val profileImgUrl: String,
    val nickname: String,
    val followState: FollowState
)
