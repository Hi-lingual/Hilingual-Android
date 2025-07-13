package com.hilingual.data.auth.datasource

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface GoogleAuthDataSource {
    suspend fun signIn(context: Context): Result<GoogleIdTokenCredential>
}