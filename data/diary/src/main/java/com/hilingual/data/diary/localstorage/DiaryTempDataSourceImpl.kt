package com.hilingual.data.diary.localstorage

import android.net.Uri
import com.hilingual.core.localstorage.DiaryTempManager
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempDataSourceImpl @Inject constructor(
    private val diaryTempManager: DiaryTempManager
) : DiaryTempDataSource {
    override suspend fun isDiaryTempExist(selectedDate: LocalDate): Boolean =
        diaryTempManager.isDiaryTempExist(selectedDate)

    override suspend fun saveDiary(
        selectedDate: LocalDate,
        text: String,
        imageUri: Uri?
    ) {
        diaryTempManager.saveDiary(selectedDate, text, imageUri)
    }

    override suspend fun getDiaryText(selectedDate: LocalDate): String? =
        diaryTempManager.getDiaryText(selectedDate)

    override suspend fun getDiaryImageUri(selectedDate: LocalDate): String? =
        diaryTempManager.getDiaryImageUri(selectedDate)

    override suspend fun clearDiaryTemp(selectedDate: LocalDate) {
        diaryTempManager.clearDiaryTemp(selectedDate)
    }
}
