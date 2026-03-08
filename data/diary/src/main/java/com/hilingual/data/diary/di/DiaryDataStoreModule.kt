package com.hilingual.data.diary.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiaryDataStoreModule {

    private const val HILINGUAL_DIARY_TEMP_PREFS = "hilingual_diary_temp_prefs"

    @Provides
    @Singleton
    @DiaryTempDataStore
    fun provideDiaryTempDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_DIARY_TEMP_PREFS
    )
}
