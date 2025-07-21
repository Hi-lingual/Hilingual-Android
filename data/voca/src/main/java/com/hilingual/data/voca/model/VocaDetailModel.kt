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
package com.hilingual.data.voca.model

import com.hilingual.data.voca.dto.response.VocaDetailResponseDto

data class VocaDetailModel(
    val phraseId: Long,
    val phrase: String,
    val phraseType: List<String>,
    val explanation: String,
    val writtenDate: String,
    val isBookmarked: Boolean
)

internal fun VocaDetailResponseDto.toModel(): VocaDetailModel =
    VocaDetailModel(
        phraseId = this.phraseId,
        phrase = this.phrase,
        phraseType = this.phraseType,
        explanation = this.explanation,
        writtenDate = this.writtenDate,
        isBookmarked = this.isBookmarked
    )
