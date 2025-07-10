package com.hilingual.data.auth.datasourceimpl

import android.content.Context
import androidx.credentials.GetCredentialResponse
import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.response.LoginResponseDto
import com.hilingual.data.auth.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val authService: AuthService
) : AuthRemoteDataSource {
    override suspend fun signInWithGoogle(context: Context): Result<GetCredentialResponse> =
        googleAuthDataSource.signIn(context)

    override suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto> =
        authService.login(providerToken, LoginRequestDto(provider))
}
