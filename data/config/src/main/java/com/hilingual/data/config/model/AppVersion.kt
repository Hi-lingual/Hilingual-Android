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
