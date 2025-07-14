package com.hilingual.data.user.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NicknameResponseDto

interface UserRemoteDataSource {
    suspend fun getNicknameAvailability(
        nickname: String
    ): BaseResponse<NicknameResponseDto>
}
