package com.hilingual.core.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.core.localstorage.di.qualifier.ReminderDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderDataStoreModule {

    private const val HILINGUAL_REMINDER_PREFS = "hilingual_reminder_prefs"

    @Provides
    @Singleton
    @ReminderDataStore
    fun provideReminderDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_REMINDER_PREFS,
    )
}
