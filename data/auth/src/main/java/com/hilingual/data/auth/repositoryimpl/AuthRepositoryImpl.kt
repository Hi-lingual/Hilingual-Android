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
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.localstorage.UserInfoManager
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.datasource.SystemDataSource
import com.hilingual.data.auth.dto.request.LoginRequestDto
import com.hilingual.data.auth.dto.request.VerifyCodeRequestDto
import com.hilingual.data.auth.model.LoginModel
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val systemDataSource: SystemDataSource,
    private val tokenManager: TokenManager,
    private val userInfoManager: UserInfoManager
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<String> =
        googleAuthDataSource.signIn(context).map { it.idToken }

    override suspend fun login(providerToken: String): Result<LoginModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.login(
            providerToken = providerToken,
            loginRequestDto = systemDataSource.toLoginRequestDto()
        ).data!!

        tokenManager.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
        userInfoManager.saveRegisterStatus(loginResponse.registerStatus)

        LoginModel(loginResponse.registerStatus)
    }

    override suspend fun verifyCode(code: String): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.verifyCode(VerifyCodeRequestDto(code))
    }

    override suspend fun getAccessToken(): String? = tokenManager.getAccessToken()

    override suspend fun getRefreshToken(): String? = tokenManager.getRefreshToken()

    override suspend fun logout(): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.logout()
        tokenManager.clearTokens()
        userInfoManager.clear()
    }

    override suspend fun withdraw(): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.withdraw()
        tokenManager.clearTokens()
        userInfoManager.clear()
    }

    private fun SystemDataSource.toLoginRequestDto() = LoginRequestDto(
        provider = this.getProvider(),
        role = this.getRole(),
        deviceName = this.getDeviceName(),
        deviceType = this.getDeviceType(),
        osType = this.getOsType(),
        osVersion = this.getOsVersion(),
        appVersion = this.getAppVersion()
    )
}
