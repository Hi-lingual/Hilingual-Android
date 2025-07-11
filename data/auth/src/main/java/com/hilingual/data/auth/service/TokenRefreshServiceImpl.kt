package com.hilingual.data.auth.service

import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.service.TokenRefreshService
import com.hilingual.core.common.util.suspendRunCatching
import javax.inject.Inject

class TokenRefreshServiceImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : TokenRefreshService {
    override suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>> = suspendRunCatching {
        val response = authService.reissueToken("Bearer $refreshToken")
        val data = response.data!!
        tokenManager.saveTokens(data.accessToken, data.refreshToken)
        Pair(data.accessToken, data.refreshToken)
    }
}
