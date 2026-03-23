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
package com.hilingual.core.localstorage.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.hilingual.core.crypto.Crypto
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class EncryptedSerializer<T>(
    private val kSerializer: KSerializer<T>,
    override val defaultValue: T,
) : Serializer<T> {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun readFrom(input: InputStream): T {
        return try {
            val base64Bytes = input.readBytes()
            if (base64Bytes.isEmpty()) {
                return defaultValue
            }

            val encryptedBytes = Base64.getDecoder().decode(base64Bytes)
            val decryptedBytes = Crypto.decrypt(encryptedBytes)
            val jsonString = decryptedBytes.decodeToString()

            json.decodeFromString(kSerializer, jsonString)
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read encrypted data.", e)
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val jsonString = json.encodeToString(kSerializer, t)
        val bytes = jsonString.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        val base64Bytes = Base64.getEncoder().encode(encryptedBytes)

        output.write(base64Bytes)
    }
}
