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
