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
