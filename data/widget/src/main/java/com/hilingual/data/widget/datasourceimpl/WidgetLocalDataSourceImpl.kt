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
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class WidgetLocalDataSourceImpl @Inject constructor(
    @WidgetDataStore private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : WidgetLocalDataSource {
    override suspend fun getTopic(): WidgetTopicModel? = dataStore.data.first()[KEY_TOPIC]
        ?.let { encoded ->
            runCatching { json.decodeFromString<WidgetTopicCache>(encoded).toModel() }.getOrNull()
        }

    override suspend fun saveTopic(topic: WidgetTopicModel) {
        dataStore.edit { preferences ->
            preferences[KEY_TOPIC] = json.encodeToString(topic.toCache())
        }
    }

    override suspend fun getStreak(): WidgetStreakModel? = dataStore.data.first()[KEY_STREAK]
        ?.let { encoded ->
            runCatching { json.decodeFromString<WidgetStreakCache>(encoded).toModel() }.getOrNull()
        }

    override suspend fun saveStreak(streak: WidgetStreakModel) {
        dataStore.edit { preferences ->
            preferences[KEY_STREAK] = json.encodeToString(streak.toCache())
        }
    }

    private companion object {
        val KEY_TOPIC = stringPreferencesKey("topic")
        val KEY_STREAK = stringPreferencesKey("streak")
    }
}
