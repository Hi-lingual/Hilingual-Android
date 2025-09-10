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
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.constant.AUTHORIZATION
import com.hilingual.core.network.constant.BEARER
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val appRestarter: AppRestarter
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            handleAuthentication(response)
        }
    }

    private suspend fun handleAuthentication(response: Response): Request? = mutex.withLock {
        val requestToken = response.request.header(AUTHORIZATION)
        Timber.d("인증 필요. 요청 토큰: $requestToken")

        // 현재 저장된 토큰과 방금 실패한 요청의 토큰을 비교합니다.
        val currentToken = tokenManager.getAccessToken()
        if (requestToken != "${BEARER} $currentToken") {
            // 다른 스레드에서 이미 토큰이 갱신되어, 현재 요청은 새 토큰으로 재시도하면 될 경우
            Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도: ${BEARER} $currentToken")
            return response.request.newBuilder()
                .header(AUTHORIZATION, "${BEARER} $currentToken")
                .build()
        }

        // 토큰 재발급 로직 실행
        val refreshToken = tokenManager.getRefreshToken() ?: run {
            Timber.d("리프레시 토큰 없음. 토큰 삭제 및 앱 재시작.")
            tokenManager.clearTokens()
            appRestarter.restartApp()
            return@withLock null
        }

        Timber.d("토큰 재발급 시도.")
        val result = tokenRefreshService.refreshToken(refreshToken)

        return if (result.isSuccess) {
            val (newAccessToken, _) = result.getOrThrow()
            Timber.d("토큰 재발급 성공. 요청 재시도.")
            response.request.newBuilder()
                .header(AUTHORIZATION, "${BEARER} $newAccessToken")
                .build()
        } else {
            Timber.d(result.exceptionOrNull(), "토큰 재발급 실패. 토큰 삭제 및 앱 재시작.")
            tokenManager.clearTokens()
            appRestarter.restartApp()
            null
        }
    }
}
