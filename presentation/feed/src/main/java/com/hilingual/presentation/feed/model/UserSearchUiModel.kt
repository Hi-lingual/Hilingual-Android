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
package com.hilingual.presentation.feed.model

import com.hilingual.data.feed.model.FollowState
import com.hilingual.data.feed.model.UserListModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal data class UserSearchUiModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val followState: FollowState
)

internal fun UserListModel.toState(): ImmutableList<UserSearchUiModel> {
    return this.userList.map {
        UserSearchUiModel(
            userId = it.userId,
            nickname = it.nickname,
            profileUrl = it.profileImg,
            followState = it.followState
        )
    }.toImmutableList()
}
