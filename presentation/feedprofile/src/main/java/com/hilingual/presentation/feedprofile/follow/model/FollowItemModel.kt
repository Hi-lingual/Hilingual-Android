package com.hilingual.presentation.feedprofile.follow.model

import androidx.compose.runtime.Immutable

@Immutable
data class FollowItemModel(
    val userId: Long,
    val profileImgUrl: String,
    val nickname: String,
    val isFollowing: Boolean,
    val isFollowed: Boolean
)

enum class FollowState(
    val actionText: String,
    val isFollowing: Boolean,
    val isFollowed: Boolean
) {
    ONLY_FOLLOWING(
        actionText = "팔로잉",
        isFollowing = true,
        isFollowed = false
    ),
    ONLY_FOLLOWED(
        actionText = "맞팔로우",
        isFollowing = false,
        isFollowed = true
    ),
    NONE(
        actionText = "팔로우",
        isFollowing = false,
        isFollowed = false
    ),
    MUTUAL_FOLLOW(
        actionText = "팔로잉",
        isFollowing = true,
        isFollowed = true
    );

    companion object {
        fun getValueByFollowState(isFollowing: Boolean, isFollowed: Boolean): FollowState? {
            return entries.find { it.isFollowing == isFollowing && it.isFollowed == isFollowed }
        }
    }
}
