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
import androidx.navigation.navOptions
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.notification.detail.NotificationDetailRoute
import com.hilingual.presentation.notification.main.NotificationRoute
import com.hilingual.presentation.notification.setting.NotificationSettingRoute
import kotlinx.serialization.Serializable

private const val ANIMATION_DURATION = 300

@Serializable
data object NotificationGraph

@Serializable
internal data object Notification : Route

@Serializable
internal data class NotificationDetail(val noticeId: Long) : Route

@Serializable
internal data object NotificationSetting : Route

fun NavController.navigateToNotification(navOptions: NavOptions? = null) =
    navigate(NotificationGraph, navOptions)

fun NavController.navigateToNotificationSetting(navOptions: NavOptions? = null) =
    navigate(NotificationSetting, navOptions)

private fun NavController.navigateToNoticeDetail(
    noticeId: Long,
    navOptions: NavOptions? = null
) = navigate(NotificationDetail(noticeId), navOptions)

fun NavGraphBuilder.notificationNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToFeedDiary: (Long) -> Unit,
    navigateToFeedProfile: (Long) -> Unit
) {
    navigation<NotificationGraph>(
        startDestination = Notification,
        enterTransition = enterTransition,
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = popExitTransition
    ) {
        composable<Notification> {
            NotificationRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToFeedDiary = navigateToFeedDiary,
                navigateToFeedProfile = navigateToFeedProfile,
                navigateToSetting = {
                    navController.navigateToNotificationSetting(
                        navOptions = navOptions { launchSingleTop = true }
                    )
                },
                navigateToNoticeDetail = { noticeId ->
                    navController.navigateToNoticeDetail(
                        noticeId = noticeId,
                        navOptions = navOptions { launchSingleTop = true }
                    )
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
