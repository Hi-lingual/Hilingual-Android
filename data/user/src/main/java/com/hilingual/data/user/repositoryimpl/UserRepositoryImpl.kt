package com.hilingual.data.user.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.repository.UserRepository
import jakarta.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun getNicknameAvailability(nickname: String): Result<Boolean> =
        suspendRunCatching {
            userRemoteDataSource.getNicknameAvailability(nickname = nickname).data!!.isAvailable
        }
}
