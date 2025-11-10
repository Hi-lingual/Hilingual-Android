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
package com.hilingual.core.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.hilingual.core.localstorage.DiaryTempManager
import com.hilingual.core.localstorage.DiaryTempManagerImpl
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.localstorage.TokenManagerImpl
import com.hilingual.core.localstorage.UserInfoManager
import com.hilingual.core.localstorage.UserInfoManagerImpl
import com.hilingual.core.localstorage.constant.DataStoreConstant
import com.hilingual.core.localstorage.model.UserPreferences
import com.hilingual.core.localstorage.serializer.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    private val Context.userInfoDataStore by preferencesDataStore(name = DataStoreConstant.HILINGUAL_USER_INFO_PREFS)
    private val Context.encryptedDataStore: DataStore<UserPreferences> by dataStore(
        fileName = DataStoreConstant.ENCRYPTED_USER_PREFS,
        serializer = UserPreferencesSerializer
    )
    private val Context.diaryTempDataStore by preferencesDataStore(name = DataStoreConstant.HILINGUAL_DIARY_TEMP_PREFS)

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManagerImpl(context.encryptedDataStore)

    @Provides
    @Singleton
    fun provideUserInfoManager(@ApplicationContext context: Context): UserInfoManager =
        UserInfoManagerImpl(context.userInfoDataStore)

    @Provides
    @Singleton
    fun provideDiaryTempManager(@ApplicationContext context: Context): DiaryTempManager =
        DiaryTempManagerImpl(dataStore = context.diaryTempDataStore, appContext = context)
}
