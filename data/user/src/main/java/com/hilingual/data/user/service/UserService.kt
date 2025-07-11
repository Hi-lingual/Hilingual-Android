package com.hilingual.data.user.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/users/profile")
    suspend fun getNicknameAvailability(
        @Query("nickname") nickname: String
    ): BaseResponse<NicknameResponseDto>
}