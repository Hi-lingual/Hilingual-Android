package com.hilingual.data.auth.localstorage

interface AuthLocalDataSource {
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun saveRegisterStatus(isCompleted: Boolean)
    suspend fun clearUserInfo()
}
