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
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.RegisterProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}
