package com.hilingual.presentation.widget.common

import com.hilingual.data.widget.repository.WidgetRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetEntryPoint {
    fun widgetRepository(): WidgetRepository
}
