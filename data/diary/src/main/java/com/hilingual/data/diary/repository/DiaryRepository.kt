package com.hilingual.data.diary.repository

import com.hilingual.data.diary.model.DiaryContentModel

interface DiaryRepository {
    suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel>
}
