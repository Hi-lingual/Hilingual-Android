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
package com.hilingual.presentation.feedprofile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feedprofile.FeedProfileRoute
import com.hilingual.presentation.feedprofile.FeedProfileViewModel
import com.hilingual.presentation.feedprofile.follow.FollowListRoute
import com.hilingual.presentation.feedprofile.follow.FollowListViewModel
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
internal data class FeedProfileGraph(val userId: Long, val showLikedDiaries: Boolean = false)

@Serializable
internal data object FeedProfile : Route

@Serializable
internal data class FollowList(val userId: Long) : Route

fun NavController.navigateToFeedProfile(userId: Long, navOptions: NavOptions? = null) =
    navigate(FeedProfileGraph(userId), navOptions)

fun NavController.navigateToMyFeedProfile(showLikedDiaries: Boolean = false, navOptions: NavOptions? = null) =
    navigate(FeedProfileGraph(userId = 0, showLikedDiaries = showLikedDiaries), navOptions)

private fun NavController.navigateToFollowList(userId: Long, navOptions: NavOptions? = null) =
    navigate(FollowList(userId), navOptions)

fun NavGraphBuilder.feedProfileNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: (Boolean) -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToFeedDiary: (Long) -> Unit
) {
    navigation<FeedProfileGraph>(
        startDestination = FeedProfile::class,
        enterTransition = enterTransition,
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None }
    ) {
        composable<FeedProfile> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(FeedProfileGraph::class)
            }
            val viewModel: FeedProfileViewModel = hiltViewModel(parentEntry)
            FeedProfileRoute(
                viewModel = viewModel,
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToMyFeedProfile = navigateToMyFeedProfile,
                navigateToFollowList = { navController.navigateToFollowList(userId = 0L) },
                navigateToFeedProfile = navigateToFeedProfile,
                navigateToFeedDiary = navigateToFeedDiary
            )
        }
        composable<FollowList>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) { backStackEntry ->
            val viewModel: FollowListViewModel = hiltViewModel(backStackEntry)
            FollowListRoute(
                viewModel = viewModel,
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToFeedProfile = navigateToFeedProfile
            )
        }
    }
}

private val enterTransition: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(ANIMATION_DURATION)
    )
}

private val popExitTransition: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(ANIMATION_DURATION)
    )
}
