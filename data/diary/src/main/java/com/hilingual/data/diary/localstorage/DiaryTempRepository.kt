package com.hilingual.data.diary.localstorage

import android.net.Uri
import java.time.LocalDate

interface DiaryTempRepository {
    suspend fun isDiaryTempExist(selectedDate: LocalDate): Result<Boolean>

    suspend fun saveDiary(selectedDate: LocalDate, text: String, imageUri: Uri?): Result<Unit>

    suspend fun getDiaryText(selectedDate: LocalDate): Result<String?>

    suspend fun getDiaryImageUri(selectedDate: LocalDate): Result<String?>

    suspend fun clearDiaryTemp(selectedDate: LocalDate): Result<Unit>
}
