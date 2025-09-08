package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.feed.model.FeedProfileModel

@Immutable
data class FeedProfileInfoModel(
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

internal fun FeedProfileModel.toState(): FeedProfileInfoModel =
    FeedProfileInfoModel(
        isMine = this.isMine,
        profileImageUrl = this.profileImageUrl,
        nickname = this.nickname,
        follower = this.follower,
        following = this.following,
        streak = this.streak,
        isFollowing = this.isFollowing,
        isFollowed = this.isFollowed,
        isBlock = this.isBlock
    )
