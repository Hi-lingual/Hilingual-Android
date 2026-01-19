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
package com.hilingual.data.config.model

import timber.log.Timber

@JvmInline
value class AppVersion(val value: String) : Comparable<AppVersion> {
    private val comparableValue: Long
        get() = try {
            val parts = value.split(".").map { it.toIntOrNull() ?: 0 }

            val major = parts.getOrElse(0) { 0 }.toLong()
            val minor = parts.getOrElse(1) { 0 }.toLong()
            val patch = parts.getOrElse(2) { 0 }.toLong()

            major * 1_000_000 + minor * 1_000 + patch
        } catch (e: Exception) {
            Timber.e(e)
            0L
        }

    override fun compareTo(other: AppVersion): Int =
        this.comparableValue.compareTo(other.comparableValue)
}
