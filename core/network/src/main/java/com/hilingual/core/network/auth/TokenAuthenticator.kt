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
package com.hilingual.core.network.auth

import com.hilingual.core.common.app.AppRestarter
import com.hilingual.core.common.di.ApplicationScope
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.constant.AUTHORIZATION
import com.hilingual.core.network.constant.BEARER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService,
    private val appRestarter: AppRestarter,
    @ApplicationScope private val appScope: CoroutineScope
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            val currentToken = tokenManager.getAccessToken()
            val requestToken = response.request.header(AUTHORIZATION)

            if (requestToken != "$BEARER $currentToken") {
                return rebuildRequestWithNewToken(response, currentToken)
            }

            val refreshToken = tokenManager.getRefreshToken() ?: return handleRefreshFailure()

            return performTokenRefresh(response, refreshToken)
        }
    }

    private fun rebuildRequestWithNewToken(response: Response, newToken: String?): Request {
        Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도: $BEARER $newToken")
        return response.request.newBuilder()
            .header(AUTHORIZATION, "$BEARER $newToken")
            .build()
    }

    private fun performTokenRefresh(response: Response, refreshToken: String): Request? {
        Timber.d("토큰 재발급 시도.")
        val result = tokenRefreshService.refreshToken(refreshToken)

        if (result.isFailure) {
            Timber.e(result.exceptionOrNull(), "토큰 재발급 실패.")
            return handleRefreshFailure()
        }

        val (newAccessToken, _) = result.getOrThrow()
        Timber.d("토큰 재발급 성공. 요청 재시도.")

        return response.request.newBuilder()
            .header(AUTHORIZATION, "$BEARER $newAccessToken")
            .build()
    }

    private fun handleRefreshFailure(): Request? {
        Timber.d("토큰 삭제 및 앱 재시작.")
        appScope.launch { tokenManager.clearTokens() }
        appRestarter.restartApp()
        return null
    }
}
