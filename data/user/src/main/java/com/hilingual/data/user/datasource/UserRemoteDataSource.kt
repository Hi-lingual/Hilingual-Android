package com.hilingual.data.user.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto

interface UserRemoteDataSource {
    suspend fun getNicknameAvailability(
        nickname: String
    ): BaseResponse<NicknameResponseDto>

    suspend fun postUserProfile(
        userProfileRequestDto: UserProfileRequestDto
    ): BaseResponse<Unit>

    suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto>
}
