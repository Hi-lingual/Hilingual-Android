package com.hilingual.presentation.feedprofile.profile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feedprofile.follow.FollowListRoute
import com.hilingual.presentation.feedprofile.profile.FeedProfileRoute
import com.hilingual.presentation.feedprofile.profile.FeedProfileViewModel
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
internal data class FeedProfileGraph(val userId: Long)

@Serializable
internal data object FeedProfile : Route

@Serializable
internal data object FollowList : Route

fun NavController.navigateToFeedProfile(userId: Long, navOptions: NavOptions? = null) =
    navigate(FeedProfileGraph(userId), navOptions)

fun NavController.navigateToMyFeedProfile(navOptions: NavOptions? = null) =
    navigate(FeedProfileGraph(0), navOptions)

private fun NavController.navigateToFollowList(navOptions: NavOptions? = null) =
    navigate(FollowList, navOptions)

fun NavGraphBuilder.feedProfileNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
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
                navigateToFollowList = { navController.navigateToFollowList() },
                navigateToFeedProfile = navigateToFeedProfile,
                navigateToFeedDiary = navigateToFeedDiary
            )
        }
        composable<FollowList>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            FollowListRoute(
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
