package com.hilingual.data.auth.datasource

import com.hilingual.core.network.auth.TokenProvider

interface AuthLocalDataSource : TokenProvider {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveRegisterStatus(isCompleted: Boolean)
    suspend fun clearUserInfo()
}
