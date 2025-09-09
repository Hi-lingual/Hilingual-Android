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
import com.hilingual.data.user.dto.request.RegisterProfileRequestDto
import com.hilingual.data.user.dto.response.notification.NotificationDetailResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationSettingsResponseDto
import com.hilingual.data.user.dto.response.user.BlockListResponseDto
import com.hilingual.data.user.dto.response.user.NicknameResponseDto
import com.hilingual.data.user.dto.response.user.UserInfoResponseDto
import com.hilingual.data.user.dto.response.user.UserLoginInfoResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserService {
    @GET("/api/v1/users/profile/check")
    suspend fun getNicknameAvailability(
        @Query("nickname") nickname: String
    ): BaseResponse<NicknameResponseDto>

    @POST("/api/v1/users/profile")
    suspend fun postUserProfile(
        @Body userProfileRequestDto: RegisterProfileRequestDto
    ): BaseResponse<Unit>

    @GET("/api/v1/users/home/info")
    suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto>

    @GET("/api/v1/users/mypage/info")
    suspend fun getUserLoginInfo(): BaseResponse<UserLoginInfoResponseDto>

    @GET("/api/v1/users/mypage/blocks")
    suspend fun getBlockList(): BaseResponse<BlockListResponseDto>

    @PUT("/api/v1/users/block/{targetUserId}")
    suspend fun putBlockUser(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<Unit>

    @DELETE("/api/v1/users/unblock/{targetUserId}")
    suspend fun deleteBlockUser(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<Unit>

    @GET("/api/v1/users/notifications")
    suspend fun getNotifications(
        @Query("tab") tab: String
    ): BaseResponse<List<NotificationResponseDto>>

    @GET("/api/v1/users/notifications/{noticeId}")
    suspend fun getNotificationDetail(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<NotificationDetailResponseDto>

    @PATCH("/api/v1/users/notifications/{noticeId}/read")
    suspend fun readNotification(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<Unit>

    @GET("/api/v1/users/mypage/noti")
    suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsResponseDto>

    @PATCH("/api/v1/users/mypage/noti")
    suspend fun updateNotificationSetting(
        @Query("notiType") notiType: String
    ): BaseResponse<NotificationSettingsResponseDto>
}
