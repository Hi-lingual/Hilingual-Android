package com.hilingual.data.auth.datasource

import android.content.Context
import androidx.credentials.GetCredentialResponse
import com.hilingual.core.network.BaseResponse
import com.hilingual.data.auth.dto.response.LoginResponseDto

interface AuthRemoteDataSource {
    suspend fun signInWithGoogle(context: Context): Result<GetCredentialResponse>
    suspend fun login(providerToken: String, provider: String): BaseResponse<LoginResponseDto>
}
