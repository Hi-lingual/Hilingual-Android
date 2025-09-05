package com.hilingual.data.user.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NotificationDetailResponseDto
import com.hilingual.data.user.dto.reponse.NotificationResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

internal interface NotificationService {
    @GET("/api/v1/users/notifications")
    suspend fun getNotifications(
        @Query("tab") tab: String
    ): BaseResponse<List<NotificationResponseDto>>

    @GET("/api/v1/users/notifications/{noticeId}")
    suspend fun getNotificationDetail(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<NotificationDetailResponseDto>

    @PATCH("/api/v1/users/notifications/{noticeId}/read")
    suspend fun readNotification(
        @Path("noticeId") noticeId: Long
    ): BaseResponse<Unit>
}
