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
package com.hilingual.core.network

import com.hilingual.core.localstorage.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidWlkIjozLCJyb2xlIjoiQURNSU4iLCJwcm92aWRlciI6IkdPT0dMRSIsInNpZCI6IjA1OTY2NGI4LTkxMDYtNGRlNS04YzAzLWYxNmY3ZTg0ZDkwZCIsInR5cGUiOiJBQ0NFU1NfVE9LRU4iLCJpYXQiOjE3NTcwOTAyNTQsImV4cCI6MTc1NzY5NTA1NH0.FLV_GZouDm4sKBfu7X5ZnPBNjUljylCRbIYoYfnAU1b_Lp-HfPRV5_-zS0M07KQ1J__xj-zNRpFWEqixfhSsSw"
        Timber.d("ACCESS_TOKEN: $token")

        val originalRequest = chain.request()

        val authRequest = originalRequest.newBuilder().newAuthBuilder(token).build()

        return chain.proceed(authRequest)
    }

    private fun Request.Builder.newAuthBuilder(accessToken: String?) =
        this.header(AUTHORIZATION, "$BEARER $accessToken")
}
