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
package com.hilingual.data.widget.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hilingual.data.widget.datasource.WidgetLocalDataSource
import com.hilingual.data.widget.di.qualifier.WidgetDataStore
import com.hilingual.data.widget.localstorage.model.WidgetStreakCache
import com.hilingual.data.widget.localstorage.model.WidgetTopicCache
import com.hilingual.data.widget.localstorage.model.toCache
import com.hilingual.data.widget.localstorage.model.toModel
import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class WidgetLocalDataSourceImpl @Inject constructor(
    @WidgetDataStore private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : WidgetLocalDataSource {
    override suspend fun getTopic(date: LocalDate): WidgetTopicModel? =
        dataStore.data.first()[KEY_TOPIC]
            ?.let { encoded ->
                runCatching { json.decodeFromString<WidgetTopicCache>(encoded).toModel() }.getOrNull()
            }
            ?.takeIf { topic -> topic.date == date.toString() }

    override suspend fun saveTopic(topic: WidgetTopicModel) {
        dataStore.edit { preferences ->
            preferences[KEY_TOPIC] = json.encodeToString(topic.toCache())
        }
    }

    override suspend fun getStreak(date: LocalDate): WidgetStreakModel? =
        dataStore.data.first()[KEY_STREAK]
            ?.let { encoded ->
                runCatching { json.decodeFromString<WidgetStreakCache>(encoded).toModel() }.getOrNull()
            }
            ?.takeIf { streak -> streak.recentDays.maxOfOrNull { it.date } == date }

    override suspend fun saveStreak(streak: WidgetStreakModel) {
        dataStore.edit { preferences ->
            preferences[KEY_STREAK] = json.encodeToString(streak.toCache())
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    private companion object {
        val KEY_TOPIC = stringPreferencesKey("topic")
        val KEY_STREAK = stringPreferencesKey("streak")
    }
}
