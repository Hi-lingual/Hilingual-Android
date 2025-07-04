package com.hilingual.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.hilingual.presentation.home.navigation.Home
import com.hilingual.presentation.home.navigation.navigateToHome
import com.hilingual.presentation.voca.navigation.navigateToVoca

internal class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Home

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(
                navOptions = navOptions
            )
            MainTab.VOCA -> navController.navigateToVoca(
                navOptions = navOptions
            )
            //TODO: 추후 스프린트 기간에 구현
            MainTab.COMMUNITY -> {}
            MainTab.MY -> {}
        }
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
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}