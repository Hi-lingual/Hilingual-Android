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
package com.hilingual.data.user.repositoryimpl

import android.content.Context
import android.net.Uri
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.hilingual.core.common.app.DeviceInfoProvider
import com.hilingual.core.common.app.FcmTokenProvider
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.util.toIsoDate
import com.hilingual.data.presigned.repository.FileUploaderRepository
import com.hilingual.data.user.datasource.UserLocalDataSource
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.dto.request.PatchFcmTokenRequestDto
import com.hilingual.data.user.dto.request.PutDeviceInfoRequestDto
import com.hilingual.data.user.model.follow.FollowUserListResultModel
import com.hilingual.data.user.model.follow.toModel
import com.hilingual.data.user.model.notification.NotificationDetailModel
import com.hilingual.data.user.model.notification.NotificationModel
import com.hilingual.data.user.model.notification.NotificationSettingsModel
import com.hilingual.data.user.model.notification.toModel
import com.hilingual.data.user.model.user.BlockListModel
import com.hilingual.data.user.model.user.NicknameValidationResult
import com.hilingual.data.user.model.user.RecoveryTicketModel
import com.hilingual.data.user.model.user.UserInfoModel
import com.hilingual.data.user.model.user.UserLoginInfoModel
import com.hilingual.data.user.model.user.UserProfileModel
import com.hilingual.data.user.model.user.toModel
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.data.user.worker.FcmTokenSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val fileUploaderRepository: FileUploaderRepository,
    private val userLocalDataSource: UserLocalDataSource,
    private val fcmTokenProvider: FcmTokenProvider,
    private val deviceInfoProvider: DeviceInfoProvider,
    @ApplicationContext private val context: Context,
) : UserRepository {
    override suspend fun getNicknameAvailability(nickname: String): Result<NicknameValidationResult> =
        suspendRunCatching {
            val response = userRemoteDataSource.getNicknameAvailability(nickname = nickname)
            when (response.code) {
                20000 -> NicknameValidationResult.AVAILABLE
                20001 -> NicknameValidationResult.DUPLICATE
                20004 -> NicknameValidationResult.FORBIDDEN_WORD
                else -> NicknameValidationResult.DUPLICATE
            }
        }

    override suspend fun postUserProfile(userProfileModel: UserProfileModel): Result<Long> =
        suspendRunCatching {
            val fileKey = if (userProfileModel.imageUri != null) {
                fileUploaderRepository.uploadFile(
                    uri = userProfileModel.imageUri,
                    purpose = "PROFILE_UPLOAD",
                ).getOrThrow()
            } else {
                null
            }

            userRemoteDataSource.postUserProfile(
                nickname = userProfileModel.nickname,
                adAlarmAgree = userProfileModel.adAlarmAgree,
                fileKey = fileKey,
            ).data!!.userId
        }

    override suspend fun getUserInfo(): Result<UserInfoModel> =
        suspendRunCatching {
            userRemoteDataSource.getUserInfo().data!!.toModel()
        }

    override suspend fun postRecoveryTicket(targetDate: LocalDate): Result<RecoveryTicketModel> =
        suspendRunCatching {
            userRemoteDataSource.postRecoveryTicket(targetDate.toIsoDate()).data!!.toModel()
        }

    override suspend fun getNotifications(tab: String): Result<List<NotificationModel>> =
        suspendRunCatching {
            userRemoteDataSource.getNotifications(tab).data!!.map { it.toModel() }
        }

    override suspend fun getNotificationDetail(noticeId: Long): Result<NotificationDetailModel> =
        suspendRunCatching {
            userRemoteDataSource.getNotificationDetail(noticeId).data!!.toModel()
        }

    override suspend fun readNotification(noticeId: Long): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.readNotification(noticeId).data
        }

    override suspend fun getNotificationSettings(): Result<NotificationSettingsModel> =
        suspendRunCatching {
            userRemoteDataSource.getNotificationSettings().data!!.toModel()
        }

    override suspend fun updateNotificationSetting(notiType: String): Result<NotificationSettingsModel> =
        suspendRunCatching {
            userRemoteDataSource.updateNotificationSetting(notiType).data!!.toModel()
        }

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        userLocalDataSource.saveRegisterStatus(isCompleted)
    }

    override suspend fun getRegisterStatus(): Boolean = userLocalDataSource.getRegisterStatus()

    @Deprecated("OTP feature is removed")
    override suspend fun saveOtpVerified(isVerified: Boolean) {
        // No-op
    }

    @Deprecated("OTP feature is removed")
    override suspend fun isOtpVerified(): Boolean = true

    override suspend fun getFollowers(targetUserId: Long): Result<List<FollowUserListResultModel>> =
        suspendRunCatching {
            userRemoteDataSource.getFollowers(targetUserId = targetUserId).data!!.userList.map { it.toModel() }
        }

    override suspend fun getFollowings(targetUserId: Long): Result<List<FollowUserListResultModel>> =
        suspendRunCatching {
            userRemoteDataSource.getFollowings(targetUserId = targetUserId).data!!.userList.map { it.toModel() }
        }

    override suspend fun getUserLoginInfo(): Result<UserLoginInfoModel> =
        suspendRunCatching {
            userRemoteDataSource.getUserLoginInfo().data!!.toModel()
        }

    override suspend fun getBlockList(): Result<BlockListModel> =
        suspendRunCatching {
            userRemoteDataSource.getBlockList().data!!.toModel()
        }

    override suspend fun putBlockUser(targetUserId: Long): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.putBlockUser(targetUserId).data
        }

    override suspend fun deleteBlockUser(targetUserId: Long): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.deleteBlockUser(targetUserId).data
        }

    override suspend fun updateProfileImage(imageFileUri: Uri?): Result<Unit> =
        suspendRunCatching {
            val fileKey = if (imageFileUri != null) {
                fileUploaderRepository.uploadFile(
                    uri = imageFileUri,
                    purpose = "PROFILE_UPDATE",
                ).getOrThrow()
            } else {
                null
            }

            userRemoteDataSource.updateProfileImage(fileKey)
        }

    override suspend fun updateNickname(nickname: String): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.updateNickname(nickname)
        }

    override suspend fun putFollow(targetUserId: Long): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.putFollow(targetUserId = targetUserId)
        }

    override suspend fun deleteFollow(targetUserId: Long): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.deleteFollow(targetUserId = targetUserId)
        }

    override suspend fun putDeviceInfo(): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.putDeviceInfo(
                putDeviceInfoRequestDto = deviceInfoProvider.toPutDeviceInfoRequestDto(),
            )
        }

    private fun DeviceInfoProvider.toPutDeviceInfoRequestDto() = PutDeviceInfoRequestDto(
        timezone = getTimezone(),
        deviceUuid = getUuid(),
        deviceName = getDeviceName(),
        deviceType = getDeviceType(),
        osType = getOsType(),
        osVersion = getOsVersion(),
        appVersion = getAppVersion(),
    )

    override suspend fun patchFcmToken(fcmToken: String): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.patchFcmToken(
                patchFcmTokenRequestDto = PatchFcmTokenRequestDto(
                    uuid = deviceInfoProvider.getUuid(),
                    fcmToken = fcmToken,
                ),
            )
        }

    override fun scheduleFcmTokenSync(fcmToken: String) {
        val workRequest = OneTimeWorkRequestBuilder<FcmTokenSyncWorker>()
            .setInputData(workDataOf(FcmTokenSyncWorker.KEY_FCM_TOKEN to fcmToken))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "fcm_token_sync",
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )
        Timber.tag("FCM_TOKEN").d("WorkManager 토큰 등록 재시도 예약")
    }

    override suspend fun syncFcmToken(): Result<Unit> =
        suspendRunCatching {
            val token = fcmTokenProvider.getToken()
            userRemoteDataSource.patchFcmToken(
                patchFcmTokenRequestDto = PatchFcmTokenRequestDto(
                    uuid = deviceInfoProvider.getUuid(),
                    fcmToken = token,
                ),
            )
        }
}
