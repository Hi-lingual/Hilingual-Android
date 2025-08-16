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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.hilingual.core.common.event.trigger.LocalDialogTrigger
import com.hilingual.core.common.event.trigger.LocalSnackbarTrigger
import com.hilingual.core.common.event.provider.LocalSystemBarsColor
import com.hilingual.core.common.event.trigger.LocalToastTrigger
import com.hilingual.core.common.event.trigger.rememberDialogTrigger
import com.hilingual.core.designsystem.component.dialog.HilingualErrorDialog
import com.hilingual.core.designsystem.component.snackbar.DiarySnackbar
import com.hilingual.core.designsystem.component.toast.TextToast
import com.hilingual.presentation.auth.navigation.authNavGraph
import com.hilingual.presentation.community.communityNavGraph
import com.hilingual.presentation.diaryfeedback.navigation.diaryFeedbackNavGraph
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import com.hilingual.presentation.diarywrite.navigation.diaryWriteNavGraph
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.main.state.MainAppState
import com.hilingual.presentation.mypage.myPageNavGraph
import com.hilingual.presentation.onboarding.navigation.onboardingGraph
import com.hilingual.presentation.otp.navigation.otpNavGraph
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXIT_MILLIS = 3000L

@Composable
internal fun MainScreen(
    appState: MainAppState
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isBottomBarVisible by appState.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()

    val systemBarsColor = LocalSystemBarsColor.current
    val activity = LocalActivity.current

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val dialogEventProvider = rememberDialogTrigger(
        show = appState.dialogStateHolder::showDialog,
        dismiss = appState.dialogStateHolder::dismissDialog
    )

    val onShowToast: (String) -> Unit = remember(coroutineScope, snackBarHostState) {
        { message ->
            coroutineScope.launch {
                snackBarHostState.currentSnackbarData?.dismiss()
                val job = launch {
                    snackBarHostState.showSnackbar(
                        message = message,
                        withDismissAction = false
                    )
                }
                delay(EXIT_MILLIS)
                job.cancel()
            }
        }
    }

    val onShowDiarySnackbar: () -> Unit = remember(coroutineScope, snackBarHostState) {
        {
            coroutineScope.launch {
                snackBarHostState.currentSnackbarData?.dismiss()
                val job = launch {
                    snackBarHostState.showSnackbar(
                        message = "일기가 게시되었어요!",
                        withDismissAction = true
                    )
                }
                delay(EXIT_MILLIS)
                job.cancel()
            }
        }
    }

    LaunchedEffect(isOffline, appState.dialogStateHolder.dialogState.isVisible) {
        if (isOffline && !appState.dialogStateHolder.dialogState.isVisible) {
            appState.dialogStateHolder.showDialog { appState.dialogStateHolder.dismissDialog() }
        }
    }

    HandleBackPressToExit(
        onShowToast = {
            onShowToast("버튼을 한번 더 누르면 앱이 종료됩니다!")
        }
    )

    CompositionLocalProvider(
        LocalDialogTrigger provides dialogEventProvider,
        LocalToastTrigger provides onShowToast,
        LocalSnackbarTrigger provides onShowDiarySnackbar
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { data ->
                    if (data.visuals.withDismissAction) {
                        DiarySnackbar(
                            message = data.visuals.message,
                            onClick = {
                                // TODO: FeedScreen으로 이동하도록 변경 to.작은나현
                                appState.navigateToHome()
                                data.dismiss()
                            },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 23.dp)
                        )
                    } else {
                        TextToast(
                            text = data.visuals.message,
                            modifier = Modifier.padding(bottom = 23.dp)
                        )
                    }
                }
            },
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
                    navigateToOnboarding = appState::navigateToOtp
                )

                otpNavGraph(
                    paddingValues = innerPadding,
                    navigateUp = appState::navigateUp,
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

            HilingualErrorDialog(
                state = appState.dialogStateHolder.dialogState,
                onDismiss = appState.dialogStateHolder::dismissDialog
            )
        }
    }

    if (activity != null) {
        systemBarsColor.Apply(activity)
    }
}

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    onShowToast: () -> Unit = {}
) {
    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= EXIT_MILLIS) {
            (context as? Activity)?.finish()
        } else {
            onShowToast()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
