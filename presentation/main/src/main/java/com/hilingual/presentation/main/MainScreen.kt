package com.hilingual.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.component.snackbar.TextSnackBar
import com.hilingual.presentation.auth.navigation.authNavGraph
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.onboarding.navigation.onboardingGraph
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXIT_MILLIS = 3000L

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    val systemBarsColor = LocalSystemBarsColor.current
    val activity = LocalActivity.current

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val onShowSnackBar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            val job = launch {
                snackBarHostState.showSnackbar(message)
            }
            delay(EXIT_MILLIS)
            job.cancel()
        }
    }

    HandleBackPressToExit(
        onShowSnackbar = {
            onShowSnackBar("버튼을 한번 더 누르면 앱이 종료됩니다!")
        }
    )

    SharedTransitionLayout {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    visible = navigator.isBottomBarVisible(),
                    tabs = MainTab.entries.toPersistentList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navigator.navController,
                startDestination = navigator.startDestination

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

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 24.dp)
    ) {
        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = { snackbarData -> TextSnackBar(text = snackbarData.visuals.message) }
        )
    }

    if (activity != null) {
        systemBarsColor.Apply(activity)
    }
}

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    onShowSnackbar: () -> Unit = {}
) {
    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= EXIT_MILLIS) {
            (context as? Activity)?.finish()
        } else {
            onShowSnackbar()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
