package com.hilingual.data.widget.datasource

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.widget.dto.response.WidgetTopicResponseDto

internal interface WidgetRemoteDataSource {
    suspend fun getTopic(date: String): BaseResponse<WidgetTopicResponseDto>
}
