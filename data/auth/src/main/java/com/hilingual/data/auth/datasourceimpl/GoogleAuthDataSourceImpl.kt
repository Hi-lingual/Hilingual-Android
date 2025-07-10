package com.hilingual.data.auth.datasourceimpl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.auth.BuildConfig
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import javax.inject.Inject

internal class GoogleAuthDataSourceImpl @Inject constructor(
    private val credentialManager: CredentialManager
) : GoogleAuthDataSource {
    override suspend fun signIn(context: Context): Result<GetCredentialResponse> =
        suspendRunCatching {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            credentialManager.getCredential(context, request)
        }
}
