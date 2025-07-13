package com.hilingual.data.auth.repository

import android.content.Context
import com.hilingual.data.auth.model.LoginModel

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): Result<String>
    suspend fun login(providerToken: String, provider: String): Result<LoginModel>
}
