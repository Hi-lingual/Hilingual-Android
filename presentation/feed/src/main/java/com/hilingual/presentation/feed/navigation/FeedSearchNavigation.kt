package com.hilingual.presentation.feed.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feed.search.FeedSearchRoute
import kotlinx.serialization.Serializable

@Serializable
data object FeedSearch : Route

fun NavGraphBuilder.feedSearchNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit
) {
    composable<FeedSearch> {
        FeedSearchRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToFeedProfile = navigateToFeedProfile
        )
    }
}
