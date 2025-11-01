package com.hilingual.core.localstorage.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val token: String? = null,
    val refreshToken: String? = null
)
