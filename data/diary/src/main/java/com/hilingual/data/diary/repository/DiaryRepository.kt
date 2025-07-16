package com.hilingual.data.diary.repository

import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import com.hilingual.data.diary.model.PhraseBookmarkModel

interface DiaryRepository {
    suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel>

    suspend fun getDiaryFeedbacks(diaryId: Long): Result<List<DiaryFeedbackModel>>

    suspend fun getDiaryRecommendExpressions(diaryId: Long): Result<List<DiaryRecommendExpressionModel>>

    suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkModel: PhraseBookmarkModel
    ): Result<Unit>
}
