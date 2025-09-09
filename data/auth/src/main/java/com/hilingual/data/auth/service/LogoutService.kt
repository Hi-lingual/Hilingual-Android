package com.hilingual.data.auth.service

import com.hilingual.core.network.BaseResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutService {
    @POST("/api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String
    ): BaseResponse<Unit>
}
