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
package com.hilingual.data.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.datasourceimpl.AuthRemoteDataSourceImpl
import com.hilingual.data.auth.datasourceimpl.GoogleAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindGoogleAuthDataSource(
        googleAuthDataSourceImpl: GoogleAuthDataSourceImpl
    ): GoogleAuthDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    internal companion object {
        @Provides
        @Singleton
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }
    }
}
