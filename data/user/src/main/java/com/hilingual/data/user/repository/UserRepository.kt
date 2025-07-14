package com.hilingual.data.user.repository

interface UserRepository {
    suspend fun getNicknameAvailability(
        nickname: String
    ): Result<Boolean>
}
