package com.hilingual.core.common.widget

interface WidgetUpdater {
    suspend fun clearCache()

    suspend fun updateAll()

    suspend fun updatePreviews()

    fun getInstalledWidgetCount(): InstalledWidgetCount
}
