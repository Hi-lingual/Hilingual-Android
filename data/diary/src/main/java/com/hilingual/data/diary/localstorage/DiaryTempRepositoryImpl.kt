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
