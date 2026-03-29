/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.data.diary.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.data.diary.di.qualifier.DiaryTempDataStore
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
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_DIARY_TEMP_PREFS,
    )
}
