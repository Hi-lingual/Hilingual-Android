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
package com.hilingual.data.user.datasouceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.dto.request.ImageRequestDto
import com.hilingual.data.user.dto.request.RegisterProfileRequestDto
import com.hilingual.data.user.dto.response.follow.FollowerResponseDto
import com.hilingual.data.user.dto.response.follow.FollowingResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationDetailResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationResponseDto
import com.hilingual.data.user.dto.response.notification.NotificationSettingsResponseDto
import com.hilingual.data.user.dto.response.user.BlockListResponseDto
import com.hilingual.data.user.dto.response.user.NicknameResponseDto
import com.hilingual.data.user.dto.response.user.UserInfoResponseDto
import com.hilingual.data.user.dto.response.user.UserLoginInfoResponseDto
import com.hilingual.data.user.service.UserService
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getNicknameAvailability(nickname: String): BaseResponse<NicknameResponseDto> =
        userService.getNicknameAvailability(nickname = nickname)

    override suspend fun postUserProfile(
        nickname: String,
        adAlarmAgree: Boolean,
        fileKey: String?
    ): BaseResponse<Unit> {
        val imageRequest = if (fileKey != null) {
            ImageRequestDto(
                fileKey = fileKey,
                purpose = "PROFILE_UPLOAD"
            )
        } else {
            null
        }
        return userService.postUserProfile(
            RegisterProfileRequestDto(
                image = imageRequest,
                nickname = nickname,
                adAlarmAgree = adAlarmAgree
            )
        )
    }

    override suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto> =
        userService.getUserInfo()

    override suspend fun getFollowers(targetUserId: Long): BaseResponse<FollowerResponseDto> =
        userService.getFollowers(targetUserId = targetUserId)

    override suspend fun getFollowings(targetUserId: Long): BaseResponse<FollowingResponseDto> =
        userService.getFollowings(targetUserId = targetUserId)

    override suspend fun putFollow(targetUserId: Long): BaseResponse<Unit> =
        userService.putFollow(targetUserId = targetUserId)

    override suspend fun deleteFollow(targetUserId: Long): BaseResponse<Unit> =
        userService.deleteFollow(targetUserId = targetUserId)

    override suspend fun getUserLoginInfo(): BaseResponse<UserLoginInfoResponseDto> =
        userService.getUserLoginInfo()

    override suspend fun getBlockList(): BaseResponse<BlockListResponseDto> =
        userService.getBlockList()

    override suspend fun putBlockUser(targetUserId: Long): BaseResponse<Unit> =
        userService.putBlockUser(targetUserId = targetUserId)

    override suspend fun deleteBlockUser(targetUserId: Long): BaseResponse<Unit> =
        userService.deleteBlockUser(targetUserId = targetUserId)

    override suspend fun putFollow(targetUserId: Long): BaseResponse<Unit> =
        userService.putFollow(targetUserId = targetUserId)

    override suspend fun deleteFollow(targetUserId: Long): BaseResponse<Unit> =
        userService.deleteFollow(targetUserId = targetUserId)

    override suspend fun getNotifications(tab: String): BaseResponse<List<NotificationResponseDto>> =
        userService.getNotifications(tab)

    override suspend fun getNotificationDetail(noticeId: Long): BaseResponse<NotificationDetailResponseDto> =
        userService.getNotificationDetail(noticeId)

    override suspend fun readNotification(noticeId: Long): BaseResponse<Unit> = userService.readNotification(noticeId)

    override suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsResponseDto> =
        userService.getNotificationSettings()

    override suspend fun updateNotificationSetting(notiType: String): BaseResponse<NotificationSettingsResponseDto> = userService.updateNotificationSetting(notiType)
}
