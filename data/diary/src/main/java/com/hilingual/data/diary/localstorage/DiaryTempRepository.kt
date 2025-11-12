package com.hilingual.data.diary.localstorage

import android.net.Uri
import com.hilingual.core.localstorage.DiaryTempManager
import java.time.LocalDate
import javax.inject.Inject

class DiaryTempRepository @Inject constructor(
    private val diaryTempManager: DiaryTempManager
) {
    suspend fun hasDiaryTemp(selectedDate: LocalDate): Boolean =
        diaryTempManager.hasDiaryTemp(selectedDate)

    suspend fun saveDiary(selectedDate: LocalDate, text: String, imageUri: Uri?) =
        diaryTempManager.saveDiary(selectedDate, text, imageUri)

    suspend fun getDiaryText(selectedDate: LocalDate): String? =
        diaryTempManager.getDiaryText(selectedDate)

    suspend fun getDiaryImageUri(selectedDate: LocalDate): String? =
        diaryTempManager.getDiaryImageUri(selectedDate)

    suspend fun clear(selectedDate: LocalDate) = diaryTempManager.clear(selectedDate)
}
