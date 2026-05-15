package com.hilingual.core.common.util

private val SPECIAL_CHAR_REGEX = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")

object NicknameValidator {

    const val MESSAGE_MIN_LENGTH = "최소 2글자 이상 입력해주세요"
    const val MESSAGE_SPECIAL_CHAR = "특수문자, 이모지는 사용이 불가능해요"
    const val MESSAGE_AVAILABLE = "사용 가능한 닉네임이에요"
    const val MESSAGE_DUPLICATE = "이미 사용중인 닉네임이에요"
    const val MESSAGE_FORBIDDEN_WORD = "금지어가 포함된 닉네임이에요"

    fun validateNickname(nickname: String): NicknameLocalValidation = when {
        nickname.isBlank() -> NicknameLocalValidation.Blank
        nickname.length < 2 -> NicknameLocalValidation.Invalid(MESSAGE_MIN_LENGTH)
        SPECIAL_CHAR_REGEX.containsMatchIn(nickname) -> NicknameLocalValidation.Invalid(MESSAGE_SPECIAL_CHAR)
        else -> NicknameLocalValidation.Valid
    }
}

enum class NicknameValidationResult {
    AVAILABLE,
    DUPLICATE,
    FORBIDDEN_WORD,
    ;

    val isValid: Boolean
        get() = this == AVAILABLE

    val message: String
        get() = when (this) {
            AVAILABLE -> NicknameValidator.MESSAGE_AVAILABLE
            DUPLICATE -> NicknameValidator.MESSAGE_DUPLICATE
            FORBIDDEN_WORD -> NicknameValidator.MESSAGE_FORBIDDEN_WORD
        }
}

sealed interface NicknameLocalValidation {
    data object Valid : NicknameLocalValidation
    data object Blank : NicknameLocalValidation
    data class Invalid(val message: String) : NicknameLocalValidation
}
