package com.hilingual.data.user.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.model.UserInfoModel
import com.hilingual.data.user.model.UserProfileModel
import com.hilingual.data.user.model.toDto
import com.hilingual.data.user.model.toModel
import com.hilingual.data.user.repository.UserRepository
import jakarta.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun getNicknameAvailability(nickname: String): Result<Boolean> =
        suspendRunCatching {
            userRemoteDataSource.getNicknameAvailability(nickname = nickname).data!!.isAvailable
        }

    override suspend fun postUserProfile(userProfileModel: UserProfileModel): Result<Unit> =
        suspendRunCatching {
            userRemoteDataSource.postUserProfile(userProfileRequestDto = userProfileModel.toDto())
        }

    override suspend fun getUserInfo(): Result<UserInfoModel> =
        suspendRunCatching {
            userRemoteDataSource.getUserInfo().data!!.toModel()
        }
}
