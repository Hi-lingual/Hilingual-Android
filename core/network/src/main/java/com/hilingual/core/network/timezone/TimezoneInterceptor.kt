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
package com.hilingual.core.network.timezone

import com.hilingual.core.network.constant.X_TIMEZONE
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

@Singleton
class TimezoneInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val timezoneId = TimeZone.getDefault().id
        Timber.d("X_TIMEZONE: $timezoneId")

        val request = chain.request().newBuilder()
            .header(X_TIMEZONE, timezoneId)
            .build()

        return chain.proceed(request)
    }
}
