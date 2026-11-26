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
package com.hilingual.data.diary.model

internal const val DIARY_TEXT_TOO_LONG_CODE = 42201
internal const val DIARY_SPECIAL_CHARACTER_CODE = 42202

class DiaryFeedbackException(val reason: Reason) : Exception("Diary feedback failed: $reason") {
    enum class Reason {
        TEXT_TOO_LONG,
        SPECIAL_CHARACTER,
        UNKNOWN,
    }

    companion object {
        // TODO: 서버 명세 확정 후 임시 오류 코드(42201, 42202)를 실제 코드로 교체한다.
        internal fun fromCode(code: Int?): DiaryFeedbackException = DiaryFeedbackException(
            when (code) {
                DIARY_TEXT_TOO_LONG_CODE -> Reason.TEXT_TOO_LONG
                DIARY_SPECIAL_CHARACTER_CODE -> Reason.SPECIAL_CHARACTER
                else -> Reason.UNKNOWN
            },
        )
    }
}
