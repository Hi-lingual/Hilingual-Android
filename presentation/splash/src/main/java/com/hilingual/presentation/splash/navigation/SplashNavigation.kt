package com.hilingual.presentation.splash.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

@Serializable
data object Splash : Route

fun NavController.navigateToSplash(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Splash,
        navOptions = navOptions
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.splashNavGraph(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable<Splash>(
        exitTransition = { fadeOut(tween(500), 0.999f) }
    ) {
        SplashRoute(
            navigateToAuth = navigateToAuth,
            navigateToHome = navigateToHome,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this
        )
    }
}
