/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.data.auth.repositoryimpl

import android.content.Context
import com.hilingual.core.common.app.DeviceInfoProvider
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.auth.datasource.AuthLocalDataSource
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.request.VerifyCodeRequestDto
import com.hilingual.data.auth.model.LoginModel
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<String> =
        googleAuthDataSource.signIn(context).map { it.idToken }

    override suspend fun login(providerToken: String): Result<LoginModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.login(
            providerToken = providerToken,
            loginRequestDto = deviceInfoProvider.toLoginRequestDto(),
        ).data!!

        authLocalDataSource.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
        authLocalDataSource.saveRegisterStatus(loginResponse.registerStatus)

        LoginModel(loginResponse.registerStatus)
    }

    override suspend fun verifyCode(code: String): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.verifyCode(VerifyCodeRequestDto(code))
    }

    override suspend fun getAccessToken(): String? = authLocalDataSource.getAccessToken()

    override suspend fun getRefreshToken(): String? = authLocalDataSource.getRefreshToken()

    override suspend fun logout(): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.logout()
        authLocalDataSource.clearTokens()
        authLocalDataSource.clearUserInfo()
    }

    override suspend fun withdraw(): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.withdraw()
        authLocalDataSource.clearTokens()
        authLocalDataSource.clearUserInfo()
    }

    private fun DeviceInfoProvider.toLoginRequestDto() = LoginRequestDto(
        provider = getProvider(),
        role = getRole(),
        deviceUuid = getUuid(),
    )
}
