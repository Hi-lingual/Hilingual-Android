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
package com.hilingual.presentation.feedprofile.follow.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.user.model.follow.FollowState
import com.hilingual.data.user.model.follow.FollowUserListResultModel

@Immutable
data class FollowItemModel(
    val userId: Long,
    val profileImgUrl: String,
    val nickname: String,
    val followState: FollowState
)

internal fun FollowUserListResultModel.toState(): FollowItemModel = FollowItemModel(
    userId = this.userId,
    profileImgUrl = this.profileImg,
    nickname = this.nickname,
    followState = this.followState
)
