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
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.model.LoginModel
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<String> =
        googleAuthDataSource.signIn(context).map { it.idToken }

    override suspend fun login(providerToken: String, provider: String): Result<LoginModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.login(providerToken, provider).data!!
        tokenManager.saveTokens(loginResponse.accessToken, loginResponse.refreshToken, loginResponse.isProfileCompleted)
        LoginModel(loginResponse.isProfileCompleted)
    }
}
