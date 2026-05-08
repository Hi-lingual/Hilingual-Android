package com.hilingual.data.notification.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.data.notification.di.qualifier.NotificationDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationDataStoreModule {

    private const val HILINGUAL_NOTIFICATION_STATE_PREFS = "hilingual_notification_state_prefs"

    @Provides
    @Singleton
    @NotificationDataStore
    fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_NOTIFICATION_STATE_PREFS
    )
}
