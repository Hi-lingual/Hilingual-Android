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
package com.hilingual.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.hilingual.core.common.extension.stateInWhileSubscribed
import com.hilingual.core.navigation.DiaryWriteMode
import com.hilingual.core.network.monitor.NetworkMonitor
import com.hilingual.presentation.auth.navigation.navigateToAuth
import com.hilingual.presentation.diaryfeedback.navigation.navigateToDiaryFeedback
import com.hilingual.presentation.diarywrite.navigation.navigateToDiaryWrite
import com.hilingual.presentation.feed.navigation.navigateToFeed
import com.hilingual.presentation.feeddiary.navigation.navigateToFeedDiary
import com.hilingual.presentation.feedprofile.navigation.navigateToFeedProfile
import com.hilingual.presentation.feedprofile.navigation.navigateToMyFeedProfile
import com.hilingual.presentation.home.navigation.navigateToHome
import com.hilingual.presentation.main.MainTab
import com.hilingual.presentation.mypage.navigation.navigateToMyPage
import com.hilingual.presentation.notification.navigation.navigateToNotification
import com.hilingual.presentation.notification.navigation.navigateToNotificationSetting
import com.hilingual.presentation.onboarding.navigation.navigateToOnboarding
import com.hilingual.presentation.signup.navigation.navigateToSignUp
import com.hilingual.presentation.splash.navigation.Splash
import com.hilingual.presentation.voca.navigation.navigateToVoca
import java.time.LocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

@Stable
internal class MainAppState(
    val navController: NavHostController,
    val dialogStateHolder: DialogStateHolder,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor
) {
    val startDestination = Splash

    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map(Boolean::not)
        .stateInWhileSubscribed(
            scope = coroutineScope,
            initialValue = false
        )

    private val currentDestination = navController.currentBackStackEntryFlow
        .map { it.destination }
        .stateInWhileSubscribed(
            scope = coroutineScope,
            initialValue = null
        )

    val currentTab: StateFlow<MainTab?> = currentDestination
        .map { destination ->
            MainTab.find { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateInWhileSubscribed(
            scope = coroutineScope,
            initialValue = null
        )

    val isBottomBarVisible: StateFlow<Boolean> = currentDestination
        .map { destination ->
            MainTab.contains { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateInWhileSubscribed(
            scope = coroutineScope,
            initialValue = false
        )

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }

        val refreshNavOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions = navOptions)
            MainTab.VOCA -> navController.navigateToVoca(navOptions = refreshNavOptions)
            MainTab.FEED -> navController.navigateToFeed(navOptions = refreshNavOptions)
            MainTab.MY -> navController.navigateToMyPage(navOptions = navOptions)
        }
    }

    private val clearStackNavOptions = navOptions {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }

    fun navigateToAuth(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToAuth(navOptions)
    }

    fun navigateToHome(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToHome(navOptions)
    }

    fun navigateToVoca(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToVoca(navOptions)
    }

    fun navigateToSignUp(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToSignUp(navOptions)
    }

    fun navigateToDiaryFeedback(
        diaryId: Long,
        navOptions: NavOptions? = null
    ) {
        navController.navigateToDiaryFeedback(diaryId, navOptions)
    }

    fun navigateToDiaryWrite(
        selectedDate: LocalDate,
        mode: DiaryWriteMode,
        navOptions: NavOptions? = navOptions {
            launchSingleTop = true
        }
    ) {
        navController.navigateToDiaryWrite(
            selectedDate = selectedDate,
            mode = mode,
            navOptions = navOptions
        )
    }

    fun navigateToNotification(
        navOptions: NavOptions = navOptions {
            launchSingleTop = true
        }
    ) {
        navController.navigateToNotification(navOptions)
    }

    fun navigateToNotificationSetting(
        navOptions: NavOptions = navOptions {
            launchSingleTop = true
        }
    ) {
        navController.navigateToNotificationSetting(navOptions)
    }

    fun navigateToFeed(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToFeed(navOptions)
    }

    fun navigateToFeedDiary(
        diaryId: Long,
        navOptions: NavOptions? = null
    ) {
        navController.navigateToFeedDiary(diaryId, navOptions)
    }

    fun navigateToFeedProfile(
        userId: Long,
        navOptions: NavOptions? = null
    ) {
        navController.navigateToFeedProfile(userId, navOptions)
    }

    fun navigateToMyFeedProfile(showLikedDiaries: Boolean = false, navOptions: NavOptions? = null) {
        navController.navigateToMyFeedProfile(showLikedDiaries, navOptions)
    }

    fun navigateToOnboarding(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToOnboarding(navOptions)
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}

@Composable
internal fun rememberMainAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    dialogStateHolder: DialogStateHolder = rememberDialogStateHolder(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainAppState = remember(navController, dialogStateHolder, coroutineScope, networkMonitor) {
    MainAppState(navController, dialogStateHolder, coroutineScope, networkMonitor)
}
