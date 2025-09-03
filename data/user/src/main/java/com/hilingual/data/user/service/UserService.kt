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
package com.hilingual.data.user.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.FeedNotificationResponseDto
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.NoticeNotificationResponseDto
import com.hilingual.data.user.dto.reponse.NotificationDetailResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserService {
    @GET("/api/v1/users/profile")
    suspend fun getNicknameAvailability(
        @Query("nickname") nickname: String
    ): BaseResponse<NicknameResponseDto>

    @POST("/api/v1/users/profile")
    suspend fun postUserProfile(
        @Body userProfileRequestDto: UserProfileRequestDto
    ): BaseResponse<Unit>

    @GET("/api/v1/users/info")
    suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto>

    @GET("/api/v1/users/notifications?tab=FEED")
    suspend fun getFeedNotifications(): BaseResponse<List<FeedNotificationResponseDto>>

    @GET("/api/v1/users/notifications?tab=NOTIFICATION")
    suspend fun getNoticeNotifications(): BaseResponse<List<NoticeNotificationResponseDto>>

    @GET("/api/v1/users/notifications/{noticeId}")
    suspend fun getNotificationDetail(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<NotificationDetailResponseDto>

    @PATCH("/api/v1/users/notifications/{noticeId}/read")
    suspend fun readNotification(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<Unit>
}
