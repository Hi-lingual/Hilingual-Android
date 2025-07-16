package com.hilingual.presentation.splash.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

@Serializable
data object Splash : Route

fun NavController.navigateToSplash() {
    navigate(
        route = Splash,
        navOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.splashNavGraph(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable<Splash>(
        exitTransition = { fadeOut(tween(500), 0.9999f) }
    ) {
        SplashRoute(
            navigateToAuth = navigateToAuth,
            navigateToHome = navigateToHome,
            navigateToOnboarding = navigateToOnboarding,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this
        )
    }
}
