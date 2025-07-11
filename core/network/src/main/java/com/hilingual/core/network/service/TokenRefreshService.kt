package com.hilingual.core.network.service

interface TokenRefreshService {
    suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>>
}
