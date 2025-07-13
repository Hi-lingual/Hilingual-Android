package com.hilingual.data.auth.service

import com.hilingual.core.network.AUTHORIZATION
import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.dto.response.ReissueTokenResponseDto
import retrofit2.http.Header
import retrofit2.http.POST

interface ReissueService {
    @POST("api/v1/auth/reissue")
    suspend fun reissueToken(
        @Header(AUTHORIZATION) refreshToken: String
    ): BaseResponse<ReissueTokenResponseDto>
}