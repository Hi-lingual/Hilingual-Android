/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.feed.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feed.FeedRoute
import com.hilingual.presentation.feed.search.FeedSearchRoute
import kotlinx.serialization.Serializable

@Serializable
data object FeedGraph

@Serializable
data object Feed : MainTabRoute

@Serializable
internal data object FeedSearch : Route

fun NavController.navigateToFeedGraph(
    navOptions: NavOptions? = null
) {
    navigate(
        route = FeedGraph,
        navOptions = navOptions
    )
}

internal fun NavController.navigateToFeedSearch(
    navOptions: NavOptions? = null
) {
    navigate(
        route = FeedSearch,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.feedNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateToFeedDiary: (Long) -> Unit
) {
    navigation<FeedGraph>(
        startDestination = Feed
    ) {
        composable<Feed> {
            FeedRoute(
                paddingValues = paddingValues,
                navigateToFeedDiary = navigateToFeedDiary,
                navigateToSearch = { navController.navigateToFeedSearch() }
            )
        }

        composable<FeedSearch> {
            FeedSearchRoute(
                paddingValues = paddingValues,
                navigateUp = { navController.popBackStack() },
                navigateToFeedProfile = {}
            )
        }
    }
}
