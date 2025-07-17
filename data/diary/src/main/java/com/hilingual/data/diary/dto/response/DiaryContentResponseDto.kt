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
