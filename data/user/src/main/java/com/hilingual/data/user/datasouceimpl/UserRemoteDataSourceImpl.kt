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
import com.hilingual.data.user.dto.reponse.BlockListResponseDto
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.reponse.UserLoginInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto
import com.hilingual.data.user.service.UserService
import jakarta.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getNicknameAvailability(nickname: String): BaseResponse<NicknameResponseDto> =
        userService.getNicknameAvailability(nickname = nickname)

    override suspend fun postUserProfile(userProfileRequestDto: UserProfileRequestDto): BaseResponse<Unit> =
        userService.postUserProfile(userProfileRequestDto = userProfileRequestDto)

    override suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto> =
        userService.getUserInfo()

    override suspend fun getUserLoginInfo(): BaseResponse<UserLoginInfoResponseDto> =
        userService.getUserLoginInfo()

    override suspend fun getBlockList(): BaseResponse<BlockListResponseDto> =
        userService.getBlockList()
}
