package com.hilingual.core.localstorage.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.localstorage.TokenManagerImpl
import com.hilingual.core.localstorage.UserInfoManager
import com.hilingual.core.localstorage.UserInfoManagerImpl
import com.hilingual.core.localstorage.constant.DataStoreConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    private val Context.tokenDataStore by preferencesDataStore(name = DataStoreConstant.HILINGUAL_PREFS)
    private val Context.userInfoDataStore by preferencesDataStore(name = DataStoreConstant.HILINGUAL_USER_INFO_PREFS)

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManagerImpl(context.tokenDataStore)

    @Provides
    @Singleton
    fun provideUserInfoManager(@ApplicationContext context: Context): UserInfoManager =
        UserInfoManagerImpl(context.userInfoDataStore)
}
