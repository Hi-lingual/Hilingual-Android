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
package com.hilingual.data.user.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.user.UserProfileRequestDto
import com.hilingual.data.user.dto.response.notification.NotificationDetailResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationSettingsResponseDto
import com.hilingual.data.user.dto.response.user.BlockListResponseDto
import com.hilingual.data.user.dto.response.user.NicknameResponseDto
import com.hilingual.data.user.dto.response.user.UserInfoResponseDto
import com.hilingual.data.user.dto.response.user.UserLoginInfoResponseDto

interface UserRemoteDataSource {
    suspend fun getNicknameAvailability(nickname: String): BaseResponse<NicknameResponseDto>

    suspend fun postUserProfile(
        nickname: String,
        adAlarmAgree: Boolean,
        fileKey: String?
    ): BaseResponse<Unit>

    suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto>

    suspend fun getUserLoginInfo(): BaseResponse<UserLoginInfoResponseDto>

    suspend fun getBlockList(): BaseResponse<BlockListResponseDto>

    suspend fun putBlockUser(targetUserId: Long): BaseResponse<Unit>

    suspend fun deleteBlockUser(targetUserId: Long): BaseResponse<Unit>

    suspend fun getNotifications(tab: String): BaseResponse<List<NotificationResponseDto>>

    suspend fun getNotificationDetail(noticeId: Long): BaseResponse<NotificationDetailResponseDto>

    suspend fun readNotification(noticeId: Long): BaseResponse<Unit>

    suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsResponseDto>

    suspend fun updateNotificationSetting(notiType: String): BaseResponse<NotificationSettingsResponseDto>
}
