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

import com.hilingual.core.common.util.toLocalDate
import com.hilingual.data.voca.dto.response.VocaDetailResponseDto
import java.time.LocalDate

data class VocaDetailModel(
    val phraseId: Long,
    val phrase: String,
    val phraseType: List<String>,
    val explanation: String,
    val writtenDate: LocalDate?,
    val savedRoot: SavedRootType,
    val isBookmarked: Boolean,
)

enum class SavedRootType(val value: Int) {
    DIARY(1),
    FEED(2),
    ELSE(3)
}

internal fun VocaDetailResponseDto.toModel(): VocaDetailModel =
    VocaDetailModel(
        phraseId = this.phraseId,
        phrase = this.phrase,
        phraseType = this.phraseType,
        explanation = this.explanation,
        writtenDate = this.writtenDate?.toLocalDate(),
        savedRoot = SavedRootType.entries.find { it.value == this.savedRoot } ?: SavedRootType.ELSE,
        isBookmarked = this.isBookmarked,
    )
