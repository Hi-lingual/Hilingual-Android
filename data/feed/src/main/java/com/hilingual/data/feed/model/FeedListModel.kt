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

import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto

data class FeedListModel(
    val feedList: List<FeedItemModel>,
    val hasFollowing: Boolean = true
)

internal fun RecommendFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() }
)

internal fun FollowingFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() },
    hasFollowing = this.haveFollowing
)
