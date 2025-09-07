package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.UserItemDto

data class UserListResultModel(
    val userId: Long,
    val nickname: String,
    val profileImg: String,
    val followState: FollowState
)

internal fun UserItemDto.toModel(): UserListResultModel = UserListResultModel(
    userId = this.userId,
    nickname = this.nickname,
    profileImg = this.profileImg,
    followState = FollowState.getValueByFollowState(this.isFollowing, this.isFollowed)!!
)

enum class FollowState(
    val label: String,
    val isFollowing: Boolean,
    val isFollowed: Boolean
) {
    ONLY_FOLLOWING(
        label = "팔로잉",
        isFollowing = true,
        isFollowed = false
    ),
    ONLY_FOLLOWED(
        label = "맞팔로우",
        isFollowing = false,
        isFollowed = true
    ),
    NONE(
        label = "팔로우",
        isFollowing = false,
        isFollowed = false
    ),
    MUTUAL_FOLLOW(
        label = "팔로잉",
        isFollowing = true,
        isFollowed = true
    );

    companion object {
        fun getValueByFollowState(isFollowing: Boolean, isFollowed: Boolean): FollowState {
            return entries.find { it.isFollowing == isFollowing && it.isFollowed == isFollowed } ?: NONE
        }
    }
}
