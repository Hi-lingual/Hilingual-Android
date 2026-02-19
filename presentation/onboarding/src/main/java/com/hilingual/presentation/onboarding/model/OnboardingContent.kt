package com.hilingual.presentation.onboarding.model

import androidx.annotation.DrawableRes

internal data class OnboardingContent(
    val text: String,
    val highlightedText: String,
    @DrawableRes val image: Int
)
