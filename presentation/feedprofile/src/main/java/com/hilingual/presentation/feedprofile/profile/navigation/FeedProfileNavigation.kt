package com.hilingual.presentation.feedprofile.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feedprofile.profile.FeedProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data class FeedProfile(
    val userId: Long,
) : Route

fun NavController.navigateToFeedProfile(
    userId: Long,
    navOptions: NavOptions? = null
) {
    navigate(
        route = FeedProfile(userId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.feedProfileNavGraph(
    paddingValues: PaddingValues,
    navigateToFeedProfile: (Long) -> Unit,
    navigateUp: () -> Unit
) {
    composable<FeedProfile> {
        FeedProfileRoute(
            paddingValues = paddingValues,
            navigateToFeedProfile = navigateToFeedProfile,
            navigateUp = navigateUp
        )
    }
}