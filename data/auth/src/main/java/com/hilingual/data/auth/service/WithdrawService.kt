package com.hilingual.data.auth.service

import com.hilingual.core.network.model.BaseResponse
import retrofit2.http.POST

interface WithdrawService {
    @POST("/api/v1/auth/leave")
    suspend fun withdraw(): BaseResponse<Unit>
}
