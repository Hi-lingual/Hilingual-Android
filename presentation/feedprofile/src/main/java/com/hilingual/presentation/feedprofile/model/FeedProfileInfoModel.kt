package com.hilingual.presentation.feedprofile.model

import androidx.compose.runtime.Immutable

@Immutable
data class FeedProfileInfoModel(
    val isMine: Boolean,
    val profileUrl: String,
    val nickname: String,
    val follower: Int,
    val following: Int,
    val streak: Int,
    val isFollowing: Boolean,
    val isFollowed: Boolean,
    val isBlock: Boolean
)
