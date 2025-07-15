package com.hilingual.data.auth.datasourceimpl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.auth.BuildConfig
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import javax.inject.Inject

internal class GoogleAuthDataSourceImpl @Inject constructor(
    private val credentialManager: CredentialManager
) : GoogleAuthDataSource {
    override suspend fun signIn(context: Context): Result<GoogleIdTokenCredential> =
        suspendRunCatching {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(true)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response = credentialManager.getCredential(context, request)
            GoogleIdTokenCredential.createFrom(response.credential.data)
        }
}
