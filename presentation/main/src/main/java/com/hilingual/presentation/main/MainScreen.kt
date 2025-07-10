package com.hilingual.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.hilingual.presentation.auth.navigation.authNavGraph
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.onboarding.navigation.onboardingGraph
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList

private const val EXIT_MILLIS = 3000L //TODO: 추후 정책에 맞게 변경 필요

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    HandleBackPressToExit()
    SharedTransitionLayout {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    visible = navigator.isBottomBarVisible(),
                    tabs = MainTab.entries.toPersistentList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navigator.navController,
                startDestination = navigator.startDestination,

            ) {
                splashNavGraph(
                    navigateToAuth = navigator::navigateToAuth,
                    navigateToHome = navigator::navigateToHome,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
                authNavGraph(
                    paddingValues = innerPadding,
                    navigateToHome = navigator::navigateToHome,
                    navigateToOnboarding = navigator::navigateToOnboarding,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
                onboardingGraph(
                    paddingValues = innerPadding,
                    navigateToHome = navigator::navigateToHome
                )
                homeNavGraph(
                    paddingValues = innerPadding
                )
                vocaNavGraph(
                    paddingValues = innerPadding
                )
            }
        }
    }
}

@Composable
private fun HandleBackPressToExit() {
    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        if (System.currentTimeMillis() - backPressedTime <= EXIT_MILLIS) {
            (context as? Activity)?.finish()
        } else {
            // TODO: 앱 종료 전 토스트 or 스낵바 표시
        }
        backPressedTime = System.currentTimeMillis()
    }
}
