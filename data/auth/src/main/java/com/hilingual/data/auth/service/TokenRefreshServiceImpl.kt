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
package com.hilingual.data.auth.service

import com.hilingual.core.common.di.ApplicationScope
import com.hilingual.core.common.di.IoDispatcher
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.auth.TokenRefreshService
import com.hilingual.core.network.constant.BEARER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class TokenRefreshServiceImpl @Inject constructor(
    private val reissueService: ReissueService,
    private val tokenManager: TokenManager,
    @ApplicationScope private val appScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TokenRefreshService {

    override fun refreshToken(refreshToken: String): Result<Pair<String, String>> = runCatching {
        synchronized(this) {
            checkIfAlreadyRefreshed(refreshToken)?.let { return@runCatching it }

            val (newAccessToken, newRefreshToken) = executeReissueRequest(refreshToken)

            saveNewTokens(newAccessToken, newRefreshToken)

            Pair(newAccessToken, newRefreshToken)
        }
    }

    private fun checkIfAlreadyRefreshed(requestRefreshToken: String): Pair<String, String>? {
        val currentRefreshToken = tokenManager.getRefreshToken()
        val currentAccessToken = tokenManager.getAccessToken()

        val isRefreshed = currentRefreshToken != requestRefreshToken &&
                currentRefreshToken != null &&
                currentAccessToken != null

        if (isRefreshed) Pair(currentAccessToken, currentRefreshToken)

        return null
    }

    private fun executeReissueRequest(refreshToken: String): Pair<String, String> {
        val response = reissueService.reissueToken("$BEARER $refreshToken").execute()

        if (!response.isSuccessful) {
            throw Exception("Token refresh failed with code: ${response.code()}")
        }

        val data = response.body()?.data
            ?: throw Exception("Token refresh data is null")

        return data.accessToken to data.refreshToken
    }

    private fun saveNewTokens(accessToken: String, refreshToken: String) {
        tokenManager.updateTokensInCache(accessToken, refreshToken)

        appScope.launch(ioDispatcher) {
            try {
                tokenManager.saveTokens(accessToken, refreshToken)
            } catch (e: Throwable) {
                Timber.e(e, "Failed to save tokens to DataStore")
            }
        }
    }
}
