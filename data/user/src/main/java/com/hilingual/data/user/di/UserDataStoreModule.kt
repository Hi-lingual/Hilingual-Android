package com.hilingual.data.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDataStoreModule {

    private const val HILINGUAL_USER_INFO_PREFS = "hilingual_user_info_prefs"

    @Provides
    @Singleton
    @UserInfoDataStore
    fun provideUserInfoDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_USER_INFO_PREFS,
    )
}
