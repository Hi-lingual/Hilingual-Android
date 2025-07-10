package com.hilingual.presentation.splash.navigation

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

fun NavGraphBuilder.splashNavGraph(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable<Splash> {
        SplashRoute(
            navigateToAuth = navigateToAuth,
            navigateToHome = navigateToHome
        )
    }
}
