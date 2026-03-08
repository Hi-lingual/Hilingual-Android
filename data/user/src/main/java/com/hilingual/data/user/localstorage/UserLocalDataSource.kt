package com.hilingual.data.user.localstorage

interface UserLocalDataSource {
    suspend fun saveRegisterStatus(isCompleted: Boolean)
    suspend fun getRegisterStatus(): Boolean
    suspend fun clear()
}
