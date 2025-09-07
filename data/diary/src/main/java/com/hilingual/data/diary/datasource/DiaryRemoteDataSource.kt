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
package com.hilingual.data.diary.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DiaryRemoteDataSource {
    suspend fun getDiaryContent(
        diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>

    suspend fun getDiaryFeedbacks(
        diaryId: Long
    ): BaseResponse<DiaryFeedbackResponseDto>

    suspend fun getDiaryRecommendExpressions(
        diaryId: Long
    ): BaseResponse<DiaryRecommendExpressionResponseDto>

    suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkRequestDto: BookmarkRequestDto
    ): BaseResponse<Unit>

    suspend fun postDiaryFeedbackCreate(
        originalText: RequestBody,
        date: RequestBody,
        imageFile: MultipartBody.Part? = null
    ): BaseResponse<DiaryFeedbackCreateResponseDto>

    suspend fun patchDiaryPublish(
        diaryId: Long
    ): BaseResponse<Unit>

    suspend fun patchDiaryUnpublish(
        diaryId: Long
    ): BaseResponse<Unit>

    suspend fun deleteDiary(
        diaryId: Long
    ): BaseResponse<Unit>
}
