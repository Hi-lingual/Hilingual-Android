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
    override suspend fun login(
        providerToken: String,
        loginRequestDto: LoginRequestDto
    ): BaseResponse<LoginResponseDto> =
        loginService.login(providerToken, loginRequestDto)
}
