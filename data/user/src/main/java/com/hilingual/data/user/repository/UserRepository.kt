package com.hilingual.data.user.repository

import com.hilingual.data.user.model.UserInfoModel
import com.hilingual.data.user.model.UserProfileModel

interface UserRepository {
    suspend fun getNicknameAvailability(
        nickname: String
    ): Result<Boolean>

    suspend fun postUserProfile(
        userProfileModel: UserProfileModel
    ): Result<Unit>

    suspend fun getUserInfo(): Result<UserInfoModel>
}
