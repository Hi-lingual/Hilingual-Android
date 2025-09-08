package com.hilingual.core.localstorage

interface UserInfoManager {
    suspend fun saveRegisterStatus(isCompleted: Boolean)
    suspend fun getRegisterStatus(): Boolean
    suspend fun saveOtpVerified(isVerified: Boolean)
    suspend fun isOtpVerified(): Boolean
    suspend fun clear()
}
