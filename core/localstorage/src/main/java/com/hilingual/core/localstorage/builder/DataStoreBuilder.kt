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
package com.hilingual.core.localstorage.builder

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hilingual.core.localstorage.serializer.EncryptedSerializer
import kotlinx.serialization.KSerializer

object DataStoreBuilder {
    fun createPreferencesDataStore(
        context: Context,
        name: String,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(name) },
    )

    fun <T> createEncryptedDataStore(
        context: Context,
        fileName: String,
        kSerializer: KSerializer<T>,
        defaultValue: T,
    ): DataStore<T> = DataStoreFactory.create(
        serializer = EncryptedSerializer(kSerializer, defaultValue),
        produceFile = { context.dataStoreFile(fileName) },
    )
}
