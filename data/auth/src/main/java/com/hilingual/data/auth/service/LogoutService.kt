package com.hilingual.data.auth.service

import com.hilingual.core.network.model.BaseResponse
import retrofit2.http.POST

interface LogoutService {
    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<Unit>
}
