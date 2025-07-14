package com.hilingual.data.user.datasouceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.service.UserService
import jakarta.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getNicknameAvailability(nickname: String): BaseResponse<NicknameResponseDto> =
        userService.getNicknameAvailability(nickname = nickname)
}
