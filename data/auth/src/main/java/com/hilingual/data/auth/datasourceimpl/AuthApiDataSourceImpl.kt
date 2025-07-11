package com.hilingual.data.auth.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.datasource.AuthApiDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.response.LoginResponseDto
import com.hilingual.data.auth.service.AuthService
import javax.inject.Inject

class AuthApiDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthApiDataSource {
    override suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto> =
        authService.login(providerToken, LoginRequestDto(provider))
}
