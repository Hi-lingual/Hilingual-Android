package com.hilingual.core.ui.model

const val NICKNAME_AVAILABLE_MESSAGE = "사용 가능한 닉네임이에요"

enum class NicknameValidationStatus {
    NONE,
    AVAILABLE,
    TOO_SHORT,
    SPECIAL_CHAR,
    DUPLICATE,
    FORBIDDEN_WORD;

    fun toMessage(): String = when (this) {
        NONE -> ""
        AVAILABLE -> NICKNAME_AVAILABLE_MESSAGE
        TOO_SHORT -> "최소 2글자 이상 입력해주세요"
        SPECIAL_CHAR -> "특수문자, 이모지는 사용이 불가능해요"
        DUPLICATE -> "이미 사용중인 닉네임이에요"
        FORBIDDEN_WORD -> "금지어가 포함된 닉네임이에요"
    }
}
