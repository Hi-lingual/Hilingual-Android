package com.hilingual.core.localstorage

interface TokenManager {
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveTokens(accessToken: String, refreshToken: String, isProfileCompleted: Boolean)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun isProfileCompleted(): Boolean
    suspend fun clearTokens()
}
