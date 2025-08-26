package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class FeedProfileInfoModel(
    val isMine: Boolean,
    val profileImageUrl: String,
    val nickname: String,
    val follower: Int,
    val following: Int,
    val streak: Int,
    val isFollowing: Boolean,
    val isFollowed: Boolean,
    val isBlock: Boolean
)