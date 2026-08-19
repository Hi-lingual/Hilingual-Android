/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.core.ui.model.user

enum class NicknameValidationStatus {
    NONE,
    AVAILABLE,
    TOO_SHORT,
    SPECIAL_CHAR,
    DUPLICATE,
    FORBIDDEN_WORD,
    ;

    fun toMessage(): String = when (this) {
        NONE -> ""
        AVAILABLE -> "사용 가능한 닉네임이에요"
        TOO_SHORT -> "최소 2글자 이상 입력해주세요"
        SPECIAL_CHAR -> "특수문자, 이모지는 사용이 불가능해요"
        DUPLICATE -> "이미 사용중인 닉네임이에요"
        FORBIDDEN_WORD -> "금지어가 포함된 닉네임이에요"
    }

    companion object {
        fun fromName(name: String): NicknameValidationStatus =
            entries.firstOrNull { it.name == name } ?: NONE
    }
}
