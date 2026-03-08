package com.hilingual.data.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.data.auth.di.quilifier.AuthDataStore
import com.hilingual.data.auth.localstorage.model.TokenPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataStoreModule {

    private const val ENCRYPTED_USER_PREFS = "encrypted_user_prefs.bin"

    @Provides
    @Singleton
    @AuthDataStore
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): DataStore<TokenPreferences> = DataStoreBuilder.createEncryptedDataStore(
        context = context,
        fileName = ENCRYPTED_USER_PREFS,
        kSerializer = TokenPreferences.serializer(),
        defaultValue = TokenPreferences()
    )
}
