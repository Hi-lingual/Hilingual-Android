package com.hilingual.data.auth.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.response.LoginResponseDto
import com.hilingual.data.auth.service.LoginService
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : AuthRemoteDataSource {
    override suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto> =
        loginService.login(providerToken, LoginRequestDto(provider))
}
