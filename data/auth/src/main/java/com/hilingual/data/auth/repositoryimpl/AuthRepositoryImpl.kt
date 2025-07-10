package com.hilingual.data.auth.repositoryimpl

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val googleAuthDataSource: GoogleAuthDataSource
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential> =
        googleAuthDataSource.signIn(context).mapCatching { result ->
            GoogleIdTokenCredential.createFrom(result.credential.data)
        }
}
