package com.hilingual.data.voca.di

import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.datasourceimpl.VocaDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsVocaDataSource(
        vocaDataSourceImpl: VocaDataSourceImpl
    ): VocaDataSource
}