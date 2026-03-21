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
package com.hilingual.data.calendar.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryThumbnailResponseDto(
    @SerialName("diaryId")
    val diaryId: Long,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("originalText")
    val originalText: String,
    @SerialName("isPublished")
    val isPublished: Boolean,
    // 피드백 화면 진입 시 getDiaryContent()로 직접 조회하므로 모델에 매핑하지 않습니다.
    @SerialName("isAdWatched")
    val isAdWatched: Boolean,
)
