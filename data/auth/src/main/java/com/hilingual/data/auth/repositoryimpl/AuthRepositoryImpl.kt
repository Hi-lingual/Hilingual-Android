package com.hilingual.data.auth.repositoryimpl

import android.content.Context
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.TokenManager
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.model.LoginModel
import com.hilingual.data.auth.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): Result<String> =
        googleAuthDataSource.signIn(context).map { it.idToken }

    override suspend fun login(providerToken: String, provider: String): Result<LoginModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.login(providerToken, provider).data!!
        tokenManager.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
        LoginModel(loginResponse.isProfileCompleted)
    }
}
