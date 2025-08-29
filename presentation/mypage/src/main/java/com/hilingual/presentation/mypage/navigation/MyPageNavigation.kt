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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.mypage.BlockedUserRoute
import com.hilingual.presentation.mypage.MyPageRoute
import com.hilingual.presentation.mypage.ProfileEditRoute
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
data object MyPage : MainTabRoute

@Serializable
internal data object MyPageMain : Route

@Serializable
internal data object ProfileEdit : Route

@Serializable
internal data object BlockedUser : Route

fun NavController.navigateToMyPage(
    navOptions: NavOptions? = null
) {
    navigate(
        route = MyPage,
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

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: () -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToAlarm: () -> Unit,
    navigateToSplash: () -> Unit
) {
    navigation<MyPage>(
        startDestination = MyPageMain,
        enterTransition = enterTransition,
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = popExitTransition
    ) {
        composable<MyPageMain> {
            MyPageRoute(
                paddingValues = paddingValues,
                navigateToProfileEdit = navController::navigateToProfileEdit,
                navigateToMyFeedProfile = navigateToMyFeedProfile,
                navigateToAlarm = navigateToAlarm,
                navigateToBlock = navController::navigateToBlockedUser,
                navigateToSplash = navigateToSplash
            )
        }

        composable<ProfileEdit>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            ProfileEditRoute(
                paddingValues = paddingValues,
                navigateToSplash = navigateToSplash
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
