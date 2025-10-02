
package com.hilingual.di

import com.hilingual.app.AppRestarterImpl
import com.hilingual.core.common.app.AppRestarter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindAppRestarter(impl: AppRestarterImpl): AppRestarter
}
