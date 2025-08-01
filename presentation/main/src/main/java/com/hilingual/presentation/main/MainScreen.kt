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

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.component.dialog.HilingualErrorDialog
import com.hilingual.core.designsystem.component.snackbar.TextSnackBar
import com.hilingual.core.designsystem.event.LocalDialogController
import com.hilingual.core.designsystem.event.rememberDialogController
import com.hilingual.presentation.auth.navigation.authNavGraph
import com.hilingual.presentation.community.communityNavGraph
import com.hilingual.presentation.diaryfeedback.navigation.diaryFeedbackNavGraph
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import com.hilingual.presentation.diarywrite.navigation.diaryWriteNavGraph
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.main.monitor.NetworkMonitor
import com.hilingual.presentation.mypage.myPageNavGraph
import com.hilingual.presentation.onboarding.navigation.onboardingGraph
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXIT_MILLIS = 3000L

@Composable
internal fun MainScreen(
    networkMonitor: NetworkMonitor,
    appState: MainAppState = rememberMainAppState(networkMonitor = networkMonitor)
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isBottomBarVisible by appState.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()

    val systemBarsColor = LocalSystemBarsColor.current
    val activity = LocalActivity.current

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val dialogController = rememberDialogController()

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

    LaunchedEffect(isOffline, dialogController.isVisible) {
        if (isOffline && !dialogController.isVisible) {
            dialogController.show { dialogController.dismiss() }
        }
    }

    HandleBackPressToExit(
        onShowSnackbar = {
            onShowSnackBar("버튼을 한번 더 누르면 앱이 종료됩니다!")
        }
    )

    CompositionLocalProvider(
        LocalDialogController provides dialogController
    ) {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    visible = isBottomBarVisible,
                    tabs = MainTab.entries.toPersistentList(),
                    currentTab = currentTab,
                    onTabSelected = appState::navigate
                )
            }
        ) { innerPadding ->
            NavHost(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                navController = appState.navController,
                startDestination = appState.startDestination

            ) {
                splashNavGraph(
                    navigateToAuth = appState::navigateToAuth,
                    navigateToHome = appState::navigateToHome,
                    navigateToOnboarding = appState::navigateToOnboarding
                )

                authNavGraph(
                    paddingValues = innerPadding,
                    navigateToHome = appState::navigateToHome,
                    navigateToOnboarding = appState::navigateToOnboarding
                )

                onboardingGraph(
                    paddingValues = innerPadding,
                    navigateToHome = appState::navigateToHome
                )

                homeNavGraph(
                    paddingValues = innerPadding,
                    navigateToDiaryFeedback = appState::navigateToDiaryFeedback,
                    navigateToDiaryWrite = appState::navigateToDiaryWrite
                )

                diaryWriteNavGraph(
                    paddingValues = innerPadding,
                    navigateUp = appState::navigateUp,
                    navigateToHome = appState::navigateToHome,
                    navigateToDiaryFeedback = { diaryId ->
                        appState.navigateToDiaryFeedback(
                            diaryId = diaryId,
                            navOptions = navOptions {
                                popUpTo<DiaryWrite> {
                                    inclusive = true
                                }
                            }
                        )
                    }
                )

                vocaNavGraph(
                    paddingValues = innerPadding,
                    navigateToHome = appState::navigateToHome
                )

                diaryFeedbackNavGraph(
                    paddingValues = innerPadding,
                    navigateUp = appState::navigateUp
                )

                communityNavGraph(
                    paddingValues = innerPadding
                )

                myPageNavGraph(
                    paddingValues = innerPadding
                )
            }

            HilingualErrorDialog(controller = dialogController)
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
