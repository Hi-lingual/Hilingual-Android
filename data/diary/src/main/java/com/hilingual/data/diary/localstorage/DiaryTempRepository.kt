package com.hilingual.data.diary.localstorage

import android.net.Uri
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempRepository @Inject constructor(
    private val diaryTempDataSource: DiaryTempDataSource
) {
    suspend fun isDiaryTempExist(selectedDate: LocalDate): Boolean =
        diaryTempDataSource.isDiaryTempExist(selectedDate)

    suspend fun saveDiary(selectedDate: LocalDate, text: String, imageUri: Uri?) =
        diaryTempDataSource.saveDiary(selectedDate, text, imageUri)

    suspend fun getDiaryText(selectedDate: LocalDate): String? =
        diaryTempDataSource.getDiaryText(selectedDate)

    suspend fun getDiaryImageUri(selectedDate: LocalDate): String? =
        diaryTempDataSource.getDiaryImageUri(selectedDate)

    suspend fun clearDiaryTemp(selectedDate: LocalDate) =
        diaryTempDataSource.clearDiaryTemp(selectedDate)
}
