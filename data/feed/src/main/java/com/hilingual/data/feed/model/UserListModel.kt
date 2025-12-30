/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.UserItemDto
import com.hilingual.data.feed.dto.response.UserResponseDto

data class UserListModel(
    val userList: List<UserModel>
)

data class UserModel(
    val userId: Long,
    val nickname: String,
    val profileImg: String,
    val followState: FollowState
)

internal fun UserResponseDto.toModel(): UserListModel = UserListModel(
    userList = this.userList.map { it.toModel() }
)

private fun UserItemDto.toModel(): UserModel = UserModel(
    userId = this.userId,
    nickname = this.nickname,
    profileImg = this.profileImg,
    followState = FollowState.getValueByFollowState(this.isFollowing, this.isFollowed)
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
