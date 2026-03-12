package com.hilingual.data.diary.repositoryimpl

import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.diary.datasource.DiaryLocalDataSource
import com.hilingual.data.diary.repository.DiaryLocalRepository
import java.time.LocalDate
import javax.inject.Inject

class DiaryLocalRepositoryImpl @Inject constructor(
    private val diaryLocalDataSource: DiaryLocalDataSource
) : DiaryLocalRepository {
    override suspend fun isDiaryTempExist(selectedDate: LocalDate): Result<Boolean> =
        suspendRunCatching {
            diaryLocalDataSource.isDiaryTempExist(selectedDate)
        }

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ): Result<Unit> =
        suspendRunCatching {
            diaryLocalDataSource.saveDiary(selectedDate, text, imageUri)
        }

    override suspend fun getDiaryText(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryLocalDataSource.getDiaryText(selectedDate)
        }

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryLocalDataSource.getDiaryImageUri(selectedDate)
        }

    override suspend fun clearDiaryTemp(selectedDate: LocalDate): Result<Unit> =
        suspendRunCatching {
            diaryLocalDataSource.clearDiaryTemp(selectedDate)
        }
}
