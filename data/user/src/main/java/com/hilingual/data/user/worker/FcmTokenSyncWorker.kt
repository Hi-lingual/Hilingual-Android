package com.hilingual.data.user.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hilingual.core.network.auth.TokenProvider
import com.hilingual.data.user.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import retrofit2.HttpException
import timber.log.Timber

@HiltWorker
class FcmTokenSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val token = inputData.getString(KEY_FCM_TOKEN)
        if (token.isNullOrBlank()) return Result.failure()

        val isLoggedIn = runCatching {
            val accessToken = tokenProvider.getAccessToken()
            val refreshToken = tokenProvider.getRefreshToken()
            !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
        }.onFailure { Timber.e(it, "Failed to read auth tokens") }
            .getOrElse { return Result.retry() }

        if (!isLoggedIn) {
            Timber.tag("FCM_TOKEN").d(
                "Not logged in yet (attempt=$runAttemptCount), retrying token sync",
            )
            return if (runAttemptCount < MAX_LOGIN_WAIT_ATTEMPTS) {
                Result.retry()
            } else {
                Timber.tag("FCM_TOKEN").e("Gave up waiting for login after $runAttemptCount attempts")
                Result.failure()
            }
        }

        return userRepository.patchFcmToken(fcmToken = token)
            .fold(
                onSuccess = { Result.success() },
                onFailure = { throwable ->
                    Timber.e(throwable, "Failed to patch FCM token")
                    if (throwable.isRetriable) Result.retry() else Result.failure()
                },
            )
    }

    private val Throwable.isRetriable: Boolean
        get() = this is IOException || (this is HttpException && code() >= 500)

    companion object {
        const val KEY_FCM_TOKEN = "fcm_token"

        private const val MAX_LOGIN_WAIT_ATTEMPTS = 10
    }
}
