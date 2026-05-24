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
package com.hilingual.core.ui.util

import com.hilingual.core.ui.model.user.NicknameLocalValidation
import com.hilingual.core.ui.model.user.NicknameLocalValidationReason

object NicknameValidator {
    private val SPECIAL_CHAR_REGEX = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")

    fun validateNickname(nickname: String): NicknameLocalValidation = when {
        nickname.isBlank() -> NicknameLocalValidation.Blank

        nickname.length < 2 -> NicknameLocalValidation.Invalid(NicknameLocalValidationReason.TOO_SHORT)

        SPECIAL_CHAR_REGEX.containsMatchIn(
            nickname,
        ) -> NicknameLocalValidation.Invalid(NicknameLocalValidationReason.SPECIAL_CHAR)

        else -> NicknameLocalValidation.Valid
    }
}
