package com.hilingual.data.auth.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.datasource.AuthApiDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.response.LoginResponseDto
import com.hilingual.data.auth.service.LoginService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : AuthApiDataSource {
    override suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto> =
        loginService.login(providerToken, LoginRequestDto(provider))
}
