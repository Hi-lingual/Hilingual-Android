package com.hilingual.data.diary.repositoryimpl

import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.diary.datasource.DiaryTempLocalDataSource
import com.hilingual.data.diary.repository.DiaryTempRepository
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempRepositoryImpl @Inject constructor(
    private val diaryTempLocalDataSource: DiaryTempLocalDataSource
) : DiaryTempRepository {
    override suspend fun isDiaryTempExist(selectedDate: LocalDate): Result<Boolean> =
        suspendRunCatching {
            diaryTempLocalDataSource.isDiaryTempExist(selectedDate)
        }

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ): Result<Unit> =
        suspendRunCatching {
            diaryTempLocalDataSource.saveDiary(selectedDate, text, imageUri)
        }

    override suspend fun getDiaryText(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryTempLocalDataSource.getDiaryText(selectedDate)
        }

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryTempLocalDataSource.getDiaryImageUri(selectedDate)
        }

    override suspend fun clearDiaryTemp(selectedDate: LocalDate): Result<Unit> =
        suspendRunCatching {
            diaryTempLocalDataSource.clearDiaryTemp(selectedDate)
        }
}
