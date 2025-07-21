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

import com.hilingual.data.voca.dto.response.VocaGroupResponseDto
import com.hilingual.data.voca.dto.response.VocaItemResponseDto
import com.hilingual.data.voca.dto.response.VocaListResponseDto

data class VocaListResultModel(
    val count: Int,
    val list: List<GroupingVocaModel>
)

data class GroupingVocaModel(
    val group: String,
    val words: List<VocaItemModel>
)

data class VocaItemModel(
    val phraseId: Long,
    val phrase: String,
    val phraseType: List<String>,
    val isBookmarked: Boolean
)

internal fun VocaListResponseDto.toModel(): VocaListResultModel =
    VocaListResultModel(
        count = this.count,
        list = this.wordList.map { it.toModel() }
    )

internal fun VocaGroupResponseDto.toModel(): GroupingVocaModel =
    GroupingVocaModel(
        group = this.group,
        words = this.words.map { it.toModel() }
    )

internal fun VocaItemResponseDto.toModel(): VocaItemModel =
    VocaItemModel(
        phraseId = this.phraseId,
        phrase = this.phrase,
        phraseType = this.phraseType,
        isBookmarked = this.isBookmarked
    )
