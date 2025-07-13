package com.hilingual.core.network

import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.service.TokenRefreshService
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
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Authenticator는 동기적으로 동작하므로, suspend 함수인 토큰 재발급 로직을 호출하기 위해
        // runBlocking을 사용하는 것이 불가피합니다.
        // OkHttp의 I/O 스레드에서 실행되므로 메인 스레드를 직접 차단하지는 않습니다.
        return runBlocking {
            handleAuthentication(response)
        }
    }

    private suspend fun handleAuthentication(response: Response): Request? = mutex.withLock {
        val requestToken = response.request.header(AUTHORIZATION)
        Timber.d("인증 필요. 요청 토큰: $requestToken")

        // 현재 저장된 토큰과 방금 실패한 요청의 토큰을 비교합니다.
        val currentToken = tokenManager.getAccessToken()
        if (requestToken != "$BEARER $currentToken") {
            // 다른 스레드에서 이미 토큰이 갱신되어, 현재 요청은 새 토큰으로 재시도하면 될 경우
            Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도: $BEARER $currentToken")
            return response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER $currentToken")
                .build()
        }

        // 토큰 재발급 로직 실행
        val refreshToken = tokenManager.getRefreshToken() ?: run {
            Timber.d("리프레시 토큰 없음. 토큰 삭제 및 로그아웃 처리.")
            tokenManager.clearTokens()
            return@withLock null
        }

        Timber.d("토큰 재발급 시도.")
        val result = tokenRefreshService.refreshToken(refreshToken)

        return if (result.isSuccess) {
            val (newAccessToken, newRefreshToken) = result.getOrThrow()
            tokenManager.saveTokens(newAccessToken, newRefreshToken)
            Timber.d("토큰 재발급 성공. 요청 재시도.")
            response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER $newAccessToken")
                .build()
        } else {
            Timber.d(result.exceptionOrNull(), "토큰 재발급 실패. 토큰 삭제 및 로그아웃 처리.")
            tokenManager.clearTokens()
            null
        }
    }
}