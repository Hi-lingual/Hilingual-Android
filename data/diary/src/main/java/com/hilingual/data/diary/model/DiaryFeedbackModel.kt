package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.response.FeedbackContent
import javax.annotation.concurrent.Immutable

@Immutable
data class DiaryFeedbackModel(
    val originalText: String,
    val rewriteText: String,
    val explain: String
)

internal fun FeedbackContent.toModel() = DiaryFeedbackModel(
    originalText = this.original,
    rewriteText = this.rewrite,
    explain = this.explain
)