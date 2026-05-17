package com.hilingual.core.ui.util

object NicknameValidator {
    private val SPECIAL_CHAR_REGEX = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")

    fun validateNickname(nickname: String): NicknameLocalValidation = when {
        nickname.isBlank() -> NicknameLocalValidation.Blank
        nickname.length < 2 -> NicknameLocalValidation.Invalid(NicknameLocalValidationReason.TOO_SHORT)
        SPECIAL_CHAR_REGEX.containsMatchIn(nickname) -> NicknameLocalValidation.Invalid(NicknameLocalValidationReason.SPECIAL_CHAR)
        else -> NicknameLocalValidation.Valid
    }
}

enum class NicknameLocalValidationReason {
    TOO_SHORT,
    SPECIAL_CHAR,
}

sealed interface NicknameLocalValidation {
    data object Blank : NicknameLocalValidation
    data object Valid : NicknameLocalValidation
    data class Invalid(val reason: NicknameLocalValidationReason) : NicknameLocalValidation
}
