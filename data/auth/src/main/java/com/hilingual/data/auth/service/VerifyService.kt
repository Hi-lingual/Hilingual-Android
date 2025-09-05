package com.hilingual.data.auth.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.dto.request.VerifyCodeRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface VerifyService {
    @POST("api/v1/auth/verify")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequestDto
    ): BaseResponse<Unit>
}
