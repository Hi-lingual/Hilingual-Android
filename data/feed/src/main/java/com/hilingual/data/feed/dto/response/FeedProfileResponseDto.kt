package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedProfileResponseDto(
    @SerialName("isMine")
    val isMine: Boolean,
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("follower")
    val follower: Int,
    @SerialName("following")
    val following: Int,
    @SerialName("streak")
    val streak: Int,
    @SerialName("isFollowing")
    val isFollowing: Boolean?,
    @SerialName("isFollowed")
    val isFollowed: Boolean?,
    @SerialName("isBlocked")
    val isBlocked: Boolean?
)