package com.hilingual.data.user.repository

import com.hilingual.data.user.model.UserProfile

interface UserRepository {
    suspend fun getNicknameAvailability(
        nickname: String
    ): Result<Boolean>

    suspend fun postUserProfile(
        userProfile: UserProfile
    ): Result<Unit>
}
