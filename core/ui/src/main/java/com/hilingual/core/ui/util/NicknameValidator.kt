package com.hilingual.core.ui.util

import com.hilingual.core.ui.model.NicknameLocalValidation
import com.hilingual.core.ui.model.NicknameLocalValidationReason

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
