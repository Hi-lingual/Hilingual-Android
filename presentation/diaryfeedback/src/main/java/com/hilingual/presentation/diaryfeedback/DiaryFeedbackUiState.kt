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
package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import com.hilingual.core.designsystem.model.DiaryContent
import com.hilingual.core.designsystem.model.FeedbackContent
import com.hilingual.core.designsystem.model.RecommendExpression
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class DiaryFeedbackUiState(
    val writtenDate: String = "",
    val isPublished: Boolean = false,
    val diaryContent: DiaryContent = DiaryContent(),
    val feedbackList: ImmutableList<FeedbackContent> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpression> = persistentListOf()
) {
    companion object {
        val Fake = DiaryFeedbackUiState(
            writtenDate = "8월 28일 목요일",
            diaryContent = DiaryContent(
                originalText = "Today my friend called to me. She sent message a few days ago, but I forgot to replying her. I apolozied to her. We promissed to playing when our university exams are all done. I hope that it’s be done fastly.",
                aiText = "Today my friend called me. She sent a message a few days ago, but I forgot to reply to her. I apologized to her. We promised to play when our university exams are all done. I hope that it’s done quickly.",
                diffRanges = persistentListOf(
                    Pair(36, 37),
                    Pair(75, 83),
                    Pair(94, 104),
                    Pair(116, 124),
                    Pair(128, 132),
                    Pair(195, 203)
                ),
                imageUrl = "https://avatars.githubusercontent.com/u/101113025?v=4"
            ),
            recommendExpressionList = persistentListOf(
                RecommendExpression(
                    phraseId = 0,
                    phraseType = persistentListOf("명사"),
                    phrase = "a few days ago",
                    explanation = "며칠 전에",
                    reason = "과거 시점을 간단히 나타낼 수 있는 일상적인 표현으로, 대화와 글 모두에서 자주 쓰입니다.",
                    isMarked = false
                ),
                RecommendExpression(
                    phraseId = 1,
                    phraseType = persistentListOf("동사", "숙어"),
                    phrase = "reply to",
                    explanation = "…에게 답장하다",
                    reason = "편지나 메시지에 답변할 때 쓰이며, 'I forgot to reply to her.'처럼 정확한 상황을 전달하는 데 적합합니다.",
                    isMarked = true
                ),
                RecommendExpression(
                    phraseId = 2,
                    phraseType = persistentListOf("전치사", "구"),
                    phrase = "all done",
                    explanation = "완전히 끝난",
                    reason = "작업이나 시험이 모두 끝났음을 간단하게 표현하는 구어체 표현입니다.",
                    isMarked = false
                )
            )
        )
    }
}

internal fun DiaryContentModel.toState() = DiaryContent(
    originalText = this.originalText,
    aiText = this.rewriteText,
    diffRanges = this.diffRanges.map {
        it.diffRange.first to it.diffRange.second
    }.toImmutableList(),
    imageUrl = this.imageUrl
)

internal fun DiaryFeedbackModel.toState() = FeedbackContent(
    originalText = this.originalText,
    feedbackText = this.rewriteText,
    explain = this.explain
)

internal fun DiaryRecommendExpressionModel.toState() = RecommendExpression(
    phraseId = this.phraseId,
    phraseType = this.phraseType.toImmutableList(),
    phrase = this.phrase,
    explanation = this.explanation,
    reason = this.reason,
    isMarked = this.isMarked
)
