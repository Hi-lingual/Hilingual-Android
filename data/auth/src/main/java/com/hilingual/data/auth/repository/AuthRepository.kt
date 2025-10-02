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
package com.hilingual.data.auth.repository

import android.content.Context
import com.hilingual.data.auth.model.LoginModel

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): Result<String>
    suspend fun login(providerToken: String): Result<LoginModel>
    suspend fun verifyCode(code: String): Result<Unit>
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun logout(): Result<Unit>
    suspend fun withdraw(): Result<Unit>
}
