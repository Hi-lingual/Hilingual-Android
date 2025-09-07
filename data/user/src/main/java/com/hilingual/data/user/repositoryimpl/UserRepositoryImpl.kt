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

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.UserInfoManager
import com.hilingual.data.presigned.repository.FileUploaderRepository
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.model.NicknameValidationResult
import com.hilingual.data.user.model.UserInfoModel
import com.hilingual.data.user.model.UserProfileModel
import com.hilingual.data.user.model.toModel
import com.hilingual.data.user.repository.UserRepository
import jakarta.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val fileUploaderRepository: FileUploaderRepository,
    private val userInfoManager: UserInfoManager
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

    override suspend fun postUserProfile(userProfileModel: UserProfileModel): Result<Unit> =
        suspendRunCatching {
            val fileKey = if (userProfileModel.imageUri != null) {
                fileUploaderRepository.uploadFile(
                    uri = userProfileModel.imageUri,
                    purpose = "PROFILE_UPLOAD"
                ).getOrThrow()
            } else {
                null
            }

            userRemoteDataSource.postUserProfile(
                nickname = userProfileModel.nickname,
                adAlarmAgree = userProfileModel.adAlarmAgree,
                fileKey = fileKey
            )
        }

    override suspend fun getUserInfo(): Result<UserInfoModel> =
        suspendRunCatching {
            userRemoteDataSource.getUserInfo().data!!.toModel()
        }

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        userInfoManager.saveRegisterStatus(isCompleted)
    }

    override suspend fun getRegisterStatus(): Boolean {
        return userInfoManager.getRegisterStatus()
    }

    override suspend fun saveOtpVerified(isVerified: Boolean) {
        userInfoManager.saveOtpVerified(isVerified)
    }

    override suspend fun isOtpVerified(): Boolean {
        return userInfoManager.isOtpVerified()
    }
}
