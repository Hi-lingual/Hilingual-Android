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
package com.hilingual.core.network

import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.BuildConfig.BASE_URL
import com.hilingual.core.network.service.TokenRefreshService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val UNIT_TAB = 4

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }

    @Provides
    @Singleton
    fun provideJsonConverter(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        var isMultipart = false
        return HttpLoggingInterceptor { message ->
            when {
                message.contains("Content-Type: multipart/form-data") -> {
                    isMultipart = true
                    Timber.tag("okhttp").d("CONNECTION INFO -> $message")
                }

                isMultipart && message.startsWith("--") -> {
                    Timber.tag("okhttp").d("CONNECTION INFO -> (multipart boundary)")
                    if (message.endsWith("--")) {
                        isMultipart = false // End of multipart content
                    }
                }

                isMultipart -> {
                    if (message.contains("Content-Disposition")) {
                        Timber.tag("okhttp").d("CONNECTION INFO -> $message")
                    } else {
                        try {
                            val json = JSONObject(message)
                            Timber.tag("okhttp").d(json.toString(UNIT_TAB))
                        } catch (e: org.json.JSONException) {
                            Timber.tag("okhttp").d("CONNECTION INFO -> (multipart content part)")
                        }
                    }
                }

                message.isJsonObject() ->
                    try {
                        Timber.tag("okhttp").d(JSONObject(message).toString(UNIT_TAB))
                    } catch (e: org.json.JSONException) {
                        Timber.tag("okhttp").d("CONNECTION INFO -> $message")
                    }

                message.isJsonArray() ->
                    try {
                        Timber.tag("okhttp").d(org.json.JSONArray(message).toString(UNIT_TAB))
                    } catch (e: org.json.JSONException) {
                        Timber.tag("okhttp").d("CONNECTION INFO -> $message")
                    }

                else -> {
                    Timber.tag("okhttp").d("CONNECTION INFO -> $message")
                }
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor = AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenManager: TokenManager, tokenRefreshService: TokenRefreshService): TokenAuthenticator = TokenAuthenticator(tokenManager, tokenRefreshService)

    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    @LoginClient
    fun provideLoginOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RefreshClient
    fun provideRefreshOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @MultipartClient
    fun provideMultipartOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(60.seconds)
        .readTimeout(60.seconds)
        .writeTimeout(60.seconds)
        .build()

    @Provides
    @Singleton
    fun provideDefaultRetrofit(
        client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()

    @Provides
    @Singleton
    @LoginClient
    fun provideLoginRetrofit(
        @LoginClient client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()

    @Provides
    @Singleton
    @RefreshClient
    fun provideRefreshRetrofit(
        @RefreshClient client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()

    @Provides
    @Singleton
    @MultipartClient
    fun provideMultipartRetrofit(
        @MultipartClient client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
}
