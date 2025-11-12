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
package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
internal data class DiaryWriteUiState(
    val hasDiaryTemp: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val topicKo: String = "",
    val topicEn: String = "",
    val diaryText: String = "",
    var diaryImageUri: Uri? = null
)
