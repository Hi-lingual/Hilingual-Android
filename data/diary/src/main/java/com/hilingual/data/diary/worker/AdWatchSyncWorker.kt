/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        val diaryId = inputData.getLong(KEY_DIARY_ID, -1L)
        if (diaryId == -1L) return Result.failure()

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
