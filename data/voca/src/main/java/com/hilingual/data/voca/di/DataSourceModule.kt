package com.hilingual.data.voca.di

import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.datasourceimpl.VocaDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsVocaDataSource(
        vocaDataSourceImpl: VocaDataSourceImpl
    ): VocaDataSource
}
