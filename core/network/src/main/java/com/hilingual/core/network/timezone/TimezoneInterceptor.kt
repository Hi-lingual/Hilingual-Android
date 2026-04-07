package com.hilingual.core.network.timezone

import com.hilingual.core.network.constant.X_TIMEZONE
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class TimezoneInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(X_TIMEZONE, TimeZone.getDefault().id)
            .build()

        return chain.proceed(request)
    }
}
