package com.hilingual.data.auth.datasource

import android.content.Context
import androidx.credentials.GetCredentialResponse

interface GoogleAuthDataSource {
    suspend fun signIn(context: Context): Result<GetCredentialResponse>
}
