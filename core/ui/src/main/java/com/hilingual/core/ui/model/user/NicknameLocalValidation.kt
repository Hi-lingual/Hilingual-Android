package com.hilingual.core.ui.model.user

sealed interface NicknameLocalValidation {
    data object Blank : NicknameLocalValidation
    data object Valid : NicknameLocalValidation
    data class Invalid(val reason: NicknameLocalValidationReason) : NicknameLocalValidation
}
