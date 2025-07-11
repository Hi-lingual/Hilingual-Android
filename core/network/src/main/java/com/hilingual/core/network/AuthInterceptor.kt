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
        // 메모리에 캐시된 토큰이 없으면 DataStore에서 가져옵니다.
        // runBlocking을 사용하는데 Interceptor가 동기적으로 동작하기 때문입니다.
        // OkHttp의 I/O 스레드에서 실행되므로 메인 스레드를 직접 차단하지는 않지만 동기적 getter를 제공하는게 제일 좋습니다.
        val token = accessToken ?: runBlocking {
            tokenManager.getAccessToken()
        }.also {
            accessToken = it
        }
        Timber.d("ACCESS_TOKEN: $token")

        val originalRequest = chain.request()

        val authRequest = originalRequest.newBuilder().newAuthBuilder(token).build()

        val response = chain.proceed(authRequest)

        // 401 응답을 받으면, 현재 토큰이 만료된 것이므로 메모리 캐시를 비웁니다.
        // 이후 TokenAuthenticator가 토큰 재발급을 시도하게 됩니다.
        if (response.code == 401) {
            accessToken = null
        }

        return response
    }

    private fun Request.Builder.newAuthBuilder(accessToken: String?) =
        this.header(AUTHORIZATION, "$BEARER $accessToken")
}
