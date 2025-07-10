package com.hilingual.core.network

import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.network.service.TokenRefreshService
import com.hilingual.core.network.BuildConfig.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
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
    fun provideLoggingInterceptor() = HttpLoggingInterceptor { message ->
        val jsonMessage = when {
            message.contains("Content-Disposition: form-data;") -> {
                val jsonStart = message.indexOf("{")
                val jsonEnd = message.lastIndexOf("}")

                if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                    message.substring(jsonStart, jsonEnd + 1)
                } else {
                    message
                }
            }

            else -> message
        }

        when {
            jsonMessage.isJsonObject() ->
                Timber.tag("okhttp").d(JSONObject(jsonMessage).toString(UNIT_TAB))

            jsonMessage.isJsonArray() ->
                Timber.tag("okhttp").d(JSONObject(jsonMessage).toString(UNIT_TAB))

            else -> {
                Timber.tag("okhttp").d("CONNECTION INFO -> $jsonMessage")
            }
        }
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor = AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenManager: TokenManager, tokenRefreshService: TokenRefreshService): TokenAuthenticator = TokenAuthenticator(tokenManager, tokenRefreshService)

    @Provides
    @Singleton
    fun provideAuthClientOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    @LoginClient
    fun provideLoginClientOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RefreshClient
    fun provideRefreshClientOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideAuthClientRetrofit(
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
    fun provideLoginClientRetrofit(
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
    fun provideRefreshClientRetrofit(
        @RefreshClient client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
}