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
package com.hilingual.data.user.repository

import com.hilingual.data.user.model.notification.NotificationDetailModel
import com.hilingual.data.user.model.notification.NotificationModel
import com.hilingual.data.user.model.notification.NotificationSettingsModel
import com.hilingual.data.user.model.user.BlockListModel
import com.hilingual.data.user.model.user.NicknameValidationResult
import com.hilingual.data.user.model.user.UserInfoModel
import com.hilingual.data.user.model.user.UserLoginInfoModel
import com.hilingual.data.user.model.user.UserProfileModel

interface UserRepository {
    suspend fun getNicknameAvailability(
        nickname: String
    ): Result<NicknameValidationResult>

    suspend fun postUserProfile(
        userProfileModel: UserProfileModel
    ): Result<Unit>

    suspend fun getUserInfo(): Result<UserInfoModel>

    suspend fun getNotifications(tab: String): Result<List<NotificationModel>>

    suspend fun getNotificationDetail(noticeId: Long): Result<NotificationDetailModel>

    suspend fun readNotification(noticeId: Long): Result<Unit>

    suspend fun getNotificationSettings(): Result<NotificationSettingsModel>

    suspend fun updateNotificationSetting(notiType: String): Result<NotificationSettingsModel>

    suspend fun saveRegisterStatus(isCompleted: Boolean)

    suspend fun getRegisterStatus(): Boolean

    suspend fun saveOtpVerified(isVerified: Boolean)

    suspend fun isOtpVerified(): Boolean

    suspend fun getUserLoginInfo(): Result<UserLoginInfoModel>

    suspend fun getBlockList(): Result<BlockListModel>

    suspend fun putBlockUser(targetUserId: Long): Result<Unit>

    suspend fun deleteBlockUser(targetUserId: Long): Result<Unit>
}
