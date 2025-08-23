package com.hilingual.presentation.feed.model

internal data class UserSearchUiModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val followState: FollowState
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
        fun getValueByFollowState(isFollowing: Boolean, isFollowed: Boolean) : FollowState? {
            return entries.find { it.isFollowing == isFollowing && it.isFollowed == isFollowed }
        }
    }
}