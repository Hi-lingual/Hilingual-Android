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
package com.hilingual.data.presigned.di

import com.hilingual.data.presigned.datasource.FileUploaderRemoteDataSource
import com.hilingual.data.presigned.datasource.PresignedUrlRemoteDataSource
import com.hilingual.data.presigned.datasourceimpl.FileUploaderRemoteDataSourceImpl
import com.hilingual.data.presigned.datasourceimpl.PresignedUrlRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPresignedUrlRemoteDataSource(
        presignedUrlRemoteDataSourceImpl: PresignedUrlRemoteDataSourceImpl
    ): PresignedUrlRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFileUploaderRemoteDataSource(
        fileUploaderRemoteDataSourceImpl: FileUploaderRemoteDataSourceImpl
    ): FileUploaderRemoteDataSource
}
