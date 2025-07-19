package com.hilingual.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.hilingual.presentation.auth.navigation.navigateToAuth
import com.hilingual.presentation.community.navigateToCommunity
import com.hilingual.presentation.diaryfeedback.navigation.navigateToDiaryFeedback
import com.hilingual.presentation.diarywrite.navigation.navigateToDiaryWrite
import com.hilingual.presentation.home.navigation.navigateToHome
import com.hilingual.presentation.mypage.navigateToMyPage
import com.hilingual.presentation.onboarding.navigation.navigateToOnboarding
import com.hilingual.presentation.splash.navigation.Splash
import com.hilingual.presentation.voca.navigation.navigateToVoca
import java.time.LocalDate

internal class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Splash

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
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

    @Composable
    fun isBottomBarVisible() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
