package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.FeedProfileResponseDto

data class FeedProfileModel(
    val isMine: Boolean,
    val profileImageUrl: String?,
    val nickname: String,
    val follower: Int,
    val following: Int,
    val streak: Int,
    val isFollowing: Boolean?,
    val isFollowed: Boolean?,
    val isBlock: Boolean?
)

internal fun FeedProfileResponseDto.toModel(): FeedProfileModel =
    FeedProfileModel(
        isMine = this.isMine,
        profileImageUrl = this.profileImg,
        nickname = this.nickname,
        follower = this.follower,
        following = this.following,
        streak = this.streak,
        isFollowing = this.isFollowing,
        isFollowed = this.isFollowed,
        isBlock = this.isBlocked
    )
