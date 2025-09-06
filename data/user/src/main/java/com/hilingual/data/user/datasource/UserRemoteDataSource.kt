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
import com.hilingual.data.user.dto.reponse.FollowerResponseDto
import com.hilingual.data.user.dto.reponse.FollowingResponseDto
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto

interface UserRemoteDataSource {
    suspend fun getNicknameAvailability(
        nickname: String
    ): BaseResponse<NicknameResponseDto>

    suspend fun postUserProfile(
        userProfileRequestDto: UserProfileRequestDto
    ): BaseResponse<Unit>

    suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto>

    suspend fun getFollowers(
        targetUserId: Long
    ): BaseResponse<FollowerResponseDto>

    suspend fun getFollowings(
        targetUserId: Long
    ): BaseResponse<FollowingResponseDto>

    suspend fun putFollow(
        targetUserId: Long
    ): BaseResponse<Unit>

    suspend fun deleteFollow(
        targetUserId: Long
    ): BaseResponse<Unit>
}
