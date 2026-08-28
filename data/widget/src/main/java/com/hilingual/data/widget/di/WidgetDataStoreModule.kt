package com.hilingual.data.widget.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.data.widget.di.qualifier.WidgetDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WidgetDataStoreModule {
    private const val HILINGUAL_WIDGET_PREFS = "hilingual_widget_prefs"

    @Provides
    @Singleton
    @WidgetDataStore
    fun provideWidgetDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_WIDGET_PREFS,
    )
}
