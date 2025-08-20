package com.hilingual.presentation.feed.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Feed : MainTabRoute

fun NavController.navigateToFeed(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Feed,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.feedNavGraph(
    paddingValues: PaddingValues
) {
    composable<Feed> {
//        FeedRoute(paddingValues = paddingValues)
    }
}
