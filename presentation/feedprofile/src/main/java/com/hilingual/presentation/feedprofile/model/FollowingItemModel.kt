package com.hilingual.presentation.feedprofile.model

import androidx.compose.runtime.Immutable

@Immutable
data class FollowingItemModel(
    val userId: Long,
    val profileImgUrl: String,
    val nickname: String,
    val isFollowing: Boolean,
    val isFollowed: Boolean
)
