package com.hilingual.data.widget.service

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.widget.dto.response.WidgetTopicResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WidgetService {
    @GET("/api/v1/widget/topic")
    suspend fun getTopic(
        @Query("date") date: String,
    ): BaseResponse<WidgetTopicResponseDto>
}
