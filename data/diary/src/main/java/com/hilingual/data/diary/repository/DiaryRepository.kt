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
package com.hilingual.data.diary.repository

import android.net.Uri
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackCreateModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import com.hilingual.data.diary.model.PhraseBookmarkModel
import java.time.LocalDate

interface DiaryRepository {
    suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel>

    suspend fun getDiaryFeedbacks(diaryId: Long): Result<List<DiaryFeedbackModel>>

    suspend fun getDiaryRecommendExpressions(diaryId: Long): Result<List<DiaryRecommendExpressionModel>>

    suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkModel: PhraseBookmarkModel
    ): Result<Unit>

    suspend fun postDiaryFeedbackCreate(
        originalText: String,
        date: LocalDate,
        imageFileUri: Uri? = null
    ): Result<DiaryFeedbackCreateModel>
}
