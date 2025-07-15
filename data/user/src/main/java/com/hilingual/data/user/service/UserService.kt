package com.hilingual.data.user.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}
