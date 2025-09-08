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
import com.hilingual.presentation.auth.navigation.navigateToAuth
import com.hilingual.presentation.diaryfeedback.navigation.navigateToDiaryFeedback
import com.hilingual.presentation.diarywrite.navigation.navigateToDiaryWrite
import com.hilingual.presentation.feed.navigation.navigateToFeed
import com.hilingual.presentation.feeddiary.navigation.navigateToFeedDiary
import com.hilingual.presentation.feedprofile.profile.navigation.navigateToFeedProfile
import com.hilingual.presentation.feedprofile.profile.navigation.navigateToMyFeedProfile
import com.hilingual.presentation.home.navigation.navigateToHome
import com.hilingual.presentation.main.MainTab
import com.hilingual.presentation.main.monitor.NetworkMonitor
import com.hilingual.presentation.mypage.navigation.navigateToMyPage
import com.hilingual.presentation.notification.navigation.navigateToNotification
import com.hilingual.presentation.notification.navigation.navigateToNotificationSetting
import com.hilingual.presentation.onboarding.navigation.navigateToOnboarding
import com.hilingual.presentation.otp.navigation.navigateToOtp
import com.hilingual.presentation.splash.navigation.Splash
import com.hilingual.presentation.voca.navigation.navigateToVoca
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

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
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val currentDestination = navController.currentBackStackEntryFlow
        .map { it.destination }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val currentTab: StateFlow<MainTab?> = currentDestination
        .map { destination ->
            MainTab.find { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val isBottomBarVisible: StateFlow<Boolean> = currentDestination
        .map { destination ->
            MainTab.contains { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
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

        val vocaNavOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions = navOptions)
            MainTab.VOCA -> navController.navigateToVoca(navOptions = vocaNavOptions)
            MainTab.FEED -> navController.navigateToFeed(navOptions = navOptions)
            MainTab.MY -> navController.navigateToMyPage(navOptions = navOptions)
        }
    }

    private val clearStackNavOptions = navOptions {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }

    fun navigateToOtp(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToOtp(navOptions)
    }

    fun navigateToAuth(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToAuth(navOptions)
    }

    fun navigateToHome(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToHome(navOptions)
    }

    fun navigateToVoca(navOptions: NavOptions? = null) {
        navController.navigateToVoca(navOptions)
    }

    fun navigateToOnboarding(navOptions: NavOptions? = clearStackNavOptions) {
        navController.navigateToOnboarding(navOptions)
    }

    fun navigateToDiaryFeedback(
        diaryId: Long,
        navOptions: NavOptions? = null
    ) {
        navController.navigateToDiaryFeedback(diaryId, navOptions)
    }

    fun navigateToDiaryWrite(
        selectedDate: LocalDate,
        navOptions: NavOptions? = navOptions {
            launchSingleTop = true
        }
    ) {
        navController.navigateToDiaryWrite(selectedDate, navOptions)
    }

    fun navigateToNotification(
        navOptions: NavOptions = navOptions {
            launchSingleTop = true
        }
    ) {
        navController.navigateToNotification(navOptions)
    }

    // TODO: 추후 마이페이지의 스택관리로 변경해주세요 to.지영
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

    fun navigateToMyFeedProfile(navOptions: NavOptions? = null) {
        navController.navigateToMyFeedProfile(navOptions)
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
