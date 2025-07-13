package com.hilingual.data.auth.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.dto.response.LoginResponseDto

interface AuthApiDataSource {
    suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto>
}
