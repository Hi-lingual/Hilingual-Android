package com.hilingual.data.auth.service

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.BEARER
import com.hilingual.core.network.service.TokenRefreshService
import javax.inject.Inject

internal class TokenRefreshServiceImpl @Inject constructor(
    private val reissueService: ReissueService,
    private val tokenManager: TokenManager
) : TokenRefreshService {
    override suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>> = suspendRunCatching {
        val response = reissueService.reissueToken("$BEARER $refreshToken")
        val data = response.data!!
        tokenManager.saveTokens(data.accessToken, data.refreshToken)
        Pair(data.accessToken, data.refreshToken)
    }
}
