package com.hilingual.data.auth.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.core.network.PROVIDER_TOKEN
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.response.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Header(PROVIDER_TOKEN) providerToken: String,
        @Body loginRequestDto: LoginRequestDto
    ): BaseResponse<LoginResponseDto>
}
