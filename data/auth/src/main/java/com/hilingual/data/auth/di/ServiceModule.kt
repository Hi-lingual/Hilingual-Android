package com.hilingual.data.auth.di

import com.hilingual.core.network.LoginClient
import com.hilingual.core.network.RefreshClient
import com.hilingual.data.auth.service.LoginService
import com.hilingual.data.auth.service.ReissueService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideLoginService(@LoginClient retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideReissueService(@RefreshClient retrofit: Retrofit): ReissueService = retrofit.create(ReissueService::class.java)
}
