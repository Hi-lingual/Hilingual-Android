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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            handleAuthenticator(response)
        }
    }

    private suspend fun handleAuthenticator(response: Response): Request? {
        return mutex.withLock {
            val currentAccessToken = tokenManager.getAccessToken()
            val requestToken = getRequestToken(response.request)

            if (isTokenRefreshed(requestToken, currentAccessToken)) {
                return@withLock buildRequestWithToken(response.request, currentAccessToken.orEmpty())
            }

            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken == null) {
                tokenManager.clearTokens()
                return@withLock null
            }

            val result = tokenRefreshService.refreshToken(refreshToken)

            if (result.isFailure) {
                tokenManager.clearTokens()
                return@withLock null
            }

            val (newAccessToken, _) = result.getOrThrow()
            tokenManager.saveTokens(newAccessToken, refreshToken)
            return buildRequestWithToken(response.request, newAccessToken)
        }
    }

    private fun getRequestToken(request: Request): String? =
        request.header(AUTHORIZATION)?.removePrefix(BEARER)?.trim()

    private fun isTokenRefreshed(requestToken: String?, currentAccessToken: String?): Boolean {
        if (currentAccessToken.isNullOrBlank()) return false

        return requestToken != currentAccessToken
    }

    private fun buildRequestWithToken(request: Request, token: String): Request =
        request.newBuilder()
            .removeHeader(AUTHORIZATION)
            .addHeader(AUTHORIZATION, "$BEARER $token")
            .build()
}