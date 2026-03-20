package com.hilingual.core.work.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.hilingual.core.work.worker.AdWatchSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WorkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : WorkRepository {

    override fun scheduleAdWatchSync(diaryId: Long) {
        val workRequest = OneTimeWorkRequestBuilder<AdWatchSyncWorker>()
            .setInputData(workDataOf(AdWatchSyncWorker.KEY_DIARY_ID to diaryId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "ad_watch_sync_$diaryId",
            ExistingWorkPolicy.KEEP,
            workRequest,
        )
    }
}
