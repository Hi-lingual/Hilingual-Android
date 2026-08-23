package com.hilingual.core.common.widget

interface WidgetUpdater {
    suspend fun updateAll()

    suspend fun updatePreviews()
}
