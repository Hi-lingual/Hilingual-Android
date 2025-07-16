package com.hilingual.data.user.datasouceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.dto.reponse.NicknameResponseDto
import com.hilingual.data.user.dto.reponse.UserInfoResponseDto
import com.hilingual.data.user.dto.request.UserProfileRequestDto
import com.hilingual.data.user.service.UserService
import jakarta.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getNicknameAvailability(nickname: String): BaseResponse<NicknameResponseDto> =
        userService.getNicknameAvailability(nickname = nickname)

    override suspend fun postUserProfile(userProfileRequestDto: UserProfileRequestDto): BaseResponse<Unit> =
        userService.postUserProfile(userProfileRequestDto = userProfileRequestDto)

    override suspend fun getUserInfo(): BaseResponse<UserInfoResponseDto> =
        userService.getUserInfo()
}
