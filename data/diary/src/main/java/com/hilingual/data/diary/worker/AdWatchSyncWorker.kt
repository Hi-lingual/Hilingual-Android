package com.hilingual.data.diary.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hilingual.data.diary.repository.DiaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import retrofit2.HttpException

@HiltWorker
class AdWatchSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val diaryRepository: DiaryRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val diaryId = inputData.keyValueMap[KEY_DIARY_ID] as? Long
            ?: return Result.failure()

        return diaryRepository.patchAdWatch(diaryId)
            .fold(
                onSuccess = { Result.success() },
                onFailure = { throwable ->
                    if (throwable.isRetriable) Result.retry() else Result.failure()
                },
            )
    }

    private val Throwable.isRetriable: Boolean
        get() = this is IOException || (this is HttpException && code() >= 500)

    companion object {
        const val KEY_DIARY_ID = "diary_id"
    }
}
