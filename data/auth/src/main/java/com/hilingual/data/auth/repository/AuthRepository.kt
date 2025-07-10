package com.hilingual.data.auth.repository

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential>
}