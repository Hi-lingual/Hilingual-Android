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
package com.hilingual.presentation.mypage.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.mypage.MyPageRoute
import com.hilingual.presentation.mypage.MyPageViewModel
import com.hilingual.presentation.mypage.blockeduser.BlockedUserRoute
import com.hilingual.presentation.mypage.licenses.OssLicensesScreen
import com.hilingual.presentation.mypage.profileedit.ProfileEditRoute
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
internal data object MyPageGraph

@Serializable
data object MyPage : MainTabRoute

@Serializable
internal data object ProfileEdit : Route

@Serializable
internal data object BlockedUser : Route

@Serializable
internal data object OssLicenses : Route

fun NavController.navigateToMyPage(
    navOptions: NavOptions? = null
) {
    navigate(
        route = MyPageGraph,
        navOptions = navOptions
    )
}

private fun NavController.navigateToProfileEdit(
    navOptions: NavOptions? = null
) {
    navigate(
        route = ProfileEdit,
        navOptions = navOptions
    )
}

private fun NavController.navigateToBlockedUser(
    navOptions: NavOptions? = null
) {
    navigate(
        route = BlockedUser,
        navOptions = navOptions
    )
}

private fun NavController.navigateToOssLicenses(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) {
    navigate(
        route = OssLicenses,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: () -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToAlarm: () -> Unit
) {
    navigation<MyPageGraph>(
        startDestination = MyPage
    ) {
        composable<MyPage> { backStackEntry ->
            val viewModel = sharedMyPageViewModel(navController, backStackEntry)

            MyPageRoute(
                paddingValues = paddingValues,
                navigateToProfileEdit = navController::navigateToProfileEdit,
                navigateToMyFeedProfile = navigateToMyFeedProfile,
                navigateToAlarm = navigateToAlarm,
                navigateToBlock = navController::navigateToBlockedUser,
                navigateToOssLicenses = navController::navigateToOssLicenses,
                viewModel = viewModel
            )
        }

        composable<ProfileEdit>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) { backStackEntry ->
            val viewModel = sharedMyPageViewModel(navController, backStackEntry)

            ProfileEditRoute(
                paddingValues = paddingValues,
                viewModel = viewModel
            )
        }

        composable<BlockedUser>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            BlockedUserRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToProfile = navigateToFeedProfile
            )
        }

        composable<OssLicenses>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            OssLicensesScreen(
                paddingValues = paddingValues,
                onBackClick = navigateUp
            )
        }
    }
}

@Composable
private fun sharedMyPageViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry
): MyPageViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(MyPageGraph)
    }
    return hiltViewModel(parentEntry)
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
