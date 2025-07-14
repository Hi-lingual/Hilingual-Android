package com.hilingual.data.diary.repository

import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackModel

interface DiaryRepository {
    suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel>

    suspend fun getDiaryFeedbacks(diaryId: Long): Result<List<DiaryFeedbackModel>>
}
