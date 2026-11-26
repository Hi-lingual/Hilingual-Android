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

internal const val MAX_DIARY_TEXT_LENGTH = 1000
internal const val MIN_FEEDBACK_REQUEST_LENGTH = 10

@Immutable
internal data class DiaryWriteUiState(
    val feedbackFailureType: DiaryFeedbackFailureType = DiaryFeedbackFailureType.UNKNOWN,
    val isDiaryTempExist: Boolean = false,
    val isRecovery: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val topicKo: String = "",
    val topicEn: String = "",
    val diaryText: String = "",
    val initialDiaryText: String = "",
    val diaryImageUri: Uri? = null,
    val initialDiaryImageUri: Uri? = null,
) {
    val hasUnsavedChanges: Boolean
        get() = diaryText != initialDiaryText || diaryImageUri != initialDiaryImageUri
}

internal enum class DiaryFeedbackFailureType(val description: String) {
    TEXT_TOO_LONG("문장 길이를 줄여서 다시 시도해 주세요."),
    SPECIAL_CHARACTER("특수문자를 삭제한 후 다시 시도해 주세요."),
    UNKNOWN("오류가 반복될 경우 고객센터로 문의해 주세요."),
}
