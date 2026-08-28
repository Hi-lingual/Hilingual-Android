package com.hilingual.data.widget.datasourceimpl

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.widget.datasource.WidgetRemoteDataSource
import com.hilingual.data.widget.dto.response.WidgetStreakResponseDto
import com.hilingual.data.widget.dto.response.WidgetTopicResponseDto
import com.hilingual.data.widget.service.WidgetService
import javax.inject.Inject

internal class WidgetRemoteDataSourceImpl @Inject constructor(
    private val widgetService: WidgetService,
) : WidgetRemoteDataSource {
    override suspend fun getTopic(date: String): BaseResponse<WidgetTopicResponseDto> =
        widgetService.getTopic(date)

    override suspend fun getStreak(date: String): BaseResponse<WidgetStreakResponseDto> =
        widgetService.getStreak(date)
}
