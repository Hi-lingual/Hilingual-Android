package com.hilingual.core.network

import com.hilingual.core.localstorage.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    @Volatile
    private var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = accessToken ?: runBlocking {
            tokenManager.getAccessToken()
        }.also {
            accessToken = it
        }
        Timber.d("ACCESS_TOKEN: $token")

        val originalRequest = chain.request()

        val authRequest = originalRequest.newBuilder().newAuthBuilder(token).build()

        val response = chain.proceed(authRequest)

        if (response.code == 401) {
            accessToken = null
        }

        return response
    }

    private fun Request.Builder.newAuthBuilder(accessToken: String?) =
        this.addHeader(AUTHORIZATION, "$BEARER $accessToken")
}