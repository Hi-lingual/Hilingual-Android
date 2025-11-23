package com.hilingual.data.diary.localstorage

import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempRepositoryImpl @Inject constructor(
    private val diaryTempDataSource: DiaryTempDataSource
) : DiaryTempRepository {
    override suspend fun isDiaryTempExist(selectedDate: LocalDate): Result<Boolean> =
        suspendRunCatching {
            diaryTempDataSource.isDiaryTempExist(selectedDate)
        }

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ): Result<Unit> =
        suspendRunCatching {
            diaryTempDataSource.saveDiary(selectedDate, text, imageUri)
        }

    override suspend fun getDiaryText(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryTempDataSource.getDiaryText(selectedDate)
        }

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): Result<String?> =
        suspendRunCatching {
            diaryTempDataSource.getDiaryImageUri(selectedDate)
        }

    override suspend fun clearDiaryTemp(selectedDate: LocalDate): Result<Unit> =
        suspendRunCatching {
            diaryTempDataSource.clearDiaryTemp(selectedDate)
        }
}
