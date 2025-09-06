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
package com.hilingual.data.diary.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

internal interface DiaryService {
    @GET("/api/v1/diaries/{diaryId}")
    suspend fun getDiaryContent(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>

    @GET("/api/v1/diaries/{diaryId}/feedbacks")
    suspend fun getDiaryFeedbacks(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryFeedbackResponseDto>

    @GET("/api/v1/diaries/{diaryId}/recommended")
    suspend fun getDiaryRecommendExpressions(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryRecommendExpressionResponseDto>

    @PATCH("/api/v1/diaries/{phraseId}")
    suspend fun patchPhraseBookmark(
        @Path("phraseId") phraseId: Long,
        @Body bookmarkRequestDto: BookmarkRequestDto
    ): BaseResponse<Unit>

    @Multipart
    @POST("/api/v1/diaries")
    suspend fun postDiaryFeedbackCreate(
        @Part("originalText") originalText: RequestBody,
        @Part("date") date: RequestBody,
        @Part imageFile: MultipartBody.Part? = null
    ): BaseResponse<DiaryFeedbackCreateResponseDto>

    @PATCH("/api/v1/diaries/{diaryId}/publish")
    suspend fun patchDiaryPublish(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<Unit>

    @PATCH("/api/v1/diaries/{diaryId}/unpublish")
    suspend fun patchDiaryUnpublish(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<Unit>

    @DELETE("/api/v1/diaries/{diaryId}")
    suspend fun deleteDiary(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<Unit>
}
