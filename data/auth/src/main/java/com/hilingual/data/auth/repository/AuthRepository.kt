package com.hilingual.data.auth.repository

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hilingual.data.auth.model.LoginModel

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential>
    suspend fun login(providerToken: String, provider: String): Result<LoginModel>
}
