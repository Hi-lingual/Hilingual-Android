package com.hilingual.data.auth.repositoryimpl

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.model.LoginModel
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential> =
        authRemoteDataSource.signInWithGoogle(context).mapCatching {
            GoogleIdTokenCredential.createFrom(it.credential.data)
        }

    override suspend fun login(providerToken: String, provider: String): Result<LoginModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.login(providerToken, provider).data!!
        tokenManager.saveAccessToken(loginResponse.accessToken)
        tokenManager.saveRefreshToken(loginResponse.refreshToken)
        LoginModel(loginResponse.isProfileCompleted)
    }
}

