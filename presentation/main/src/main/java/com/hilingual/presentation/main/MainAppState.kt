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
package com.hilingual.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.hilingual.core.designsystem.event.DialogState
import com.hilingual.presentation.auth.navigation.navigateToAuth
import com.hilingual.presentation.community.navigateToCommunity
import com.hilingual.presentation.diaryfeedback.navigation.navigateToDiaryFeedback
import com.hilingual.presentation.diarywrite.navigation.navigateToDiaryWrite
import com.hilingual.presentation.home.navigation.navigateToHome
import com.hilingual.presentation.main.monitor.NetworkMonitor
import com.hilingual.presentation.mypage.navigateToMyPage
import com.hilingual.presentation.onboarding.navigation.navigateToOnboarding
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

    val currentTab = navController.currentBackStackEntryFlow.map { backStackEntry ->
        MainTab.find { tab ->
            backStackEntry.destination.hasRoute(tab::class)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    val isBottomBarVisible = navController.currentBackStackEntryFlow.map { backStackEntry ->
        MainTab.contains { tab ->
            backStackEntry.destination.hasRoute(tab::class)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    var dialogState by mutableStateOf(DialogState())
        private set

    fun showDialog(onClick: () -> Unit) {
        dialogState = DialogState(isVisible = true, onClickAction = onClick)
    }

    fun dismissDialog() {
        dialogState = dialogState.copy(isVisible = false)
    }

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
            MainTab.COMMUNITY -> navController.navigateToCommunity(navOptions = navOptions)
            MainTab.MY -> navController.navigateToMyPage(navOptions = navOptions)
        }
    }

    fun navigateToAuth(
        navOptions: NavOptions? = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    ) {
        navController.navigateToAuth(navOptions)
    }

    fun navigateToHome(
        navOptions: NavOptions? = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    ) {
        navController.navigateToHome(navOptions)
    }

    fun navigateToOnboarding(
        navOptions: NavOptions? = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    ) {
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

    fun navigateUp() {
        navController.navigateUp()
    }
}

@Composable
internal fun rememberMainAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainAppState = remember(navController, coroutineScope, networkMonitor) {
    MainAppState(navController, coroutineScope, networkMonitor)
}
