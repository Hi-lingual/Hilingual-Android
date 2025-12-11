/*
 * Copyright 2025 The Hilingual Project
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
