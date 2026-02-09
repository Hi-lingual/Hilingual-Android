package com.hilingual.presentation.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.onboarding.OnboardingRoute
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding : Route

fun NavController.navigateToOnboarding(
    navOptions: NavOptions? = null
) {
    navigate(route = Onboarding, navOptions = navOptions)
}

fun NavGraphBuilder.onboardingNavGraph(
    navigateToAuth: () -> Unit
) {
    composable<Onboarding> {
        OnboardingRoute(navigateToAuth = navigateToAuth)
    }
}
