package com.hilingual.presentation.feed.model

import com.hilingual.data.feed.model.FollowState
import com.hilingual.data.feed.model.UserListModel

internal data class UserSearchUiModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val followState: FollowState
)

internal fun UserListModel.toState(): List<UserSearchUiModel> {
    return this.userList.map {
        UserSearchUiModel(
            userId = it.userId,
            nickname = it.nickname,
            profileUrl = it.profileImg,
            followState = it.followState
        )
    }
}