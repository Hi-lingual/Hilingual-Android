package com.hilingual.data.user.datasource

interface UserLocalDataSource {
    suspend fun saveRegisterStatus(isCompleted: Boolean)
    suspend fun getRegisterStatus(): Boolean
    suspend fun clear()
}
