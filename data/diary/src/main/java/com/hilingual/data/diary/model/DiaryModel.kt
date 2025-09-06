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
package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.response.DiaryContentResponseDto

data class DiaryContentModel(
    val writtenDate: String,
    val originalText: String,
    val rewriteText: String,
    val diffRanges: List<DiaryContentFeedback>,
    val imageUrl: String?,
    val isPublished: Boolean
)

data class DiaryContentFeedback(
    val diffRange: Pair<Int, Int>
)

internal fun DiaryContentResponseDto.toModel() = DiaryContentModel(
    writtenDate = this.date,
    originalText = this.originalText,
    rewriteText = this.rewriteText,
    imageUrl = this.imageUrl,
    diffRanges = this.diffRanges.map {
        DiaryContentFeedback(
            Pair(it.start, it.end)
        )
    },
    isPublished = this.isPublished
)
