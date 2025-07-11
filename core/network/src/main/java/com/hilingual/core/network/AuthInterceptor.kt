package com.hilingual.core.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("ACCESS_TOKEN: ${BuildConfig.TEST_TOKEN}")

        val originalRequest = chain.request()

        val authRequest = originalRequest.newBuilder().newAuthBuilder().build()

        val response = chain.proceed(authRequest)

        return response
    }

    private fun Request.Builder.newAuthBuilder() =
        this.addHeader(AUTHORIZATION, "$BEARER ${BuildConfig.TEST_TOKEN}")
}
