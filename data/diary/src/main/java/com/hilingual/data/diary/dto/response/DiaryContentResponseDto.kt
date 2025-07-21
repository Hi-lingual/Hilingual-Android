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
package com.hilingual.data.diary.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryContentResponseDto(
    @SerialName("date")
    val date: String,
    @SerialName("originalText")
    val originalText: String,
    @SerialName("rewriteText")
    val rewriteText: String,
    @SerialName("diffRanges")
    val diffRanges: List<DiaryContentDiffRange>,
    @SerialName("imageUrl")
    val imageUrl: String?
)

@Serializable
data class DiaryContentDiffRange(
    @SerialName("start")
    val start: Int,
    @SerialName("end")
    val end: Int,
    @SerialName("correctedText")
    val correctedText: String
)
