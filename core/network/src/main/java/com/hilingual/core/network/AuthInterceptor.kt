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
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenManager.getAccessToken() }
        Timber.d("ACCESS_TOKEN: $accessToken")

        val originalRequest = chain.request()

        val authRequest = originalRequest.newBuilder().newAuthBuilder(accessToken).build()

        val response = chain.proceed(authRequest)

        return response
    }

    private fun Request.Builder.newAuthBuilder(accessToken: String?) =
        this.addHeader(AUTHORIZATION, "$BEARER $accessToken")
}