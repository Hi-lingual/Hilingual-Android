package com.hilingual.presentation.notification.navigation

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
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.notification.detail.NotificationDetailRoute
import com.hilingual.presentation.notification.main.NotificationMainRoute
import com.hilingual.presentation.notification.setting.NotificationSettingRoute
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
internal data object NotificationGraph : Route

@Serializable
internal data object NotificationMain : Route

@Serializable
internal data class NotificationDetail(val noticeId: Long) : Route

@Serializable
internal data object NotificationSetting : Route

fun NavController.navigateToNotificationGraph(navOptions: NavOptions? = null) =
    navigate(NotificationGraph, navOptions)

fun NavController.navigateToNotificationSetting(navOptions: NavOptions? = null) =
    navigate(NotificationSetting, navOptions)

fun NavController.navigateToNotificationDetail(
    noticeId: Long,
    navOptions: NavOptions? = null
) = navigate(NotificationDetail(noticeId), navOptions)

fun NavGraphBuilder.notificationNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToFeedNotificationDetail: (String) -> Unit
) {
    navigation<NotificationGraph>(
        startDestination = NotificationMain,
        enterTransition = enterTransition,
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = popExitTransition
    ) {
        composable<NotificationMain> {
            NotificationMainRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToSetting = { navController.navigateToNotificationSetting() },
                navigateToFeedNotificationDetail = navigateToFeedNotificationDetail,
                navigateToNoticeDetail = { noticeId ->
                    navController.navigateToNotificationDetail(noticeId)
                }
            )
        }

        composable<NotificationDetail>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            NotificationDetailRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp
            )
        }

        composable<NotificationSetting>(
            enterTransition = enterTransition,
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = popExitTransition
        ) {
            NotificationSettingRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp
            )
        }
    }
}

private val enterTransition: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DURATION))
}

private val popExitTransition: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DURATION))
}
