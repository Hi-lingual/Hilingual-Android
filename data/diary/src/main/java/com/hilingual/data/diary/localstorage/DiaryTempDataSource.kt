package com.hilingual.data.diary.localstorage

import android.net.Uri
import java.time.LocalDate

interface DiaryTempDataSource {
    suspend fun isDiaryTempExist(selectedDate: LocalDate): Boolean
    suspend fun saveDiary(selectedDate: LocalDate, text: String, imageUri: Uri?)
    suspend fun getDiaryText(selectedDate: LocalDate): String?
    suspend fun getDiaryImageUri(selectedDate: LocalDate): String?
    suspend fun clearDiaryTemp(selectedDate: LocalDate)
}
