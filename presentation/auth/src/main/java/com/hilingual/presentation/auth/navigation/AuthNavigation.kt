package com.hilingual.presentation.auth.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.auth.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
data object Auth : Route

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Auth,
        navOptions = navOptions
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.authNavGraph(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable<Auth> {
        AuthRoute(
            paddingValues = paddingValues,
            navigateToHome = navigateToHome,
            navigateToOnboarding = navigateToOnboarding,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this
        )
    }
}
