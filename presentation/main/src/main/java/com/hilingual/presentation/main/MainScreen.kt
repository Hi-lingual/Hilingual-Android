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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.app.AppRestarter
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.model.HilingualMessage.Snackbar
import com.hilingual.core.common.model.HilingualMessage.Toast
import com.hilingual.core.common.model.MessageDuration
import com.hilingual.core.common.provider.LocalAppRestarter
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.trigger.rememberDialogTrigger
import com.hilingual.core.designsystem.component.dialog.HilingualErrorDialog
import com.hilingual.core.designsystem.component.snackbar.HilingualActionSnackbar
import com.hilingual.core.designsystem.component.toast.TextToast
import com.hilingual.presentation.auth.navigation.authNavGraph
import com.hilingual.presentation.diaryfeedback.navigation.diaryFeedbackNavGraph
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import com.hilingual.presentation.diarywrite.navigation.diaryWriteNavGraph
import com.hilingual.presentation.feed.navigation.feedNavGraph
import com.hilingual.presentation.feeddiary.navigation.feedDiaryNavGraph
import com.hilingual.presentation.feedprofile.navigation.feedProfileNavGraph
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.main.state.MainAppState
import com.hilingual.presentation.mypage.navigation.myPageNavGraph
import com.hilingual.presentation.notification.navigation.notificationNavGraph
import com.hilingual.presentation.signup.navigation.signUpGraph
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    appState: MainAppState,
    tracker: Tracker,
    appRestarter: AppRestarter
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isBottomBarVisible by appState.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val dialogTrigger = rememberDialogTrigger(
        show = appState.dialogStateHolder::showDialog
    )

    val snackBarHostState = remember { SnackbarHostState() }

    val onShowMessage: (HilingualMessage) -> Unit =
        remember(
            key1 = coroutineScope,
            key2 = snackBarHostState
        ) {
            { message ->
                coroutineScope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    val job = launch {
                        snackBarHostState.showSnackbar(visuals = message)
                    }
                    delay(message.messageDuration.millis)
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
        onShowMessage = { onShowMessage(Toast("버튼을 한번 더 누르면 앱이 종료됩니다.")) }
    )

    CompositionLocalProvider(
        LocalDialogTrigger provides dialogTrigger,
        LocalMessageController provides onShowMessage,
        LocalTracker provides tracker,
        LocalAppRestarter provides appRestarter
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
                    navigateToHome = appState::navigateToHome
                )

                authNavGraph(
                    paddingValues = innerPadding,
                    navigateToHome = appState::navigateToHome,
                    navigateToSignUp = appState::navigateToSignUp
                )

                signUpGraph(
                    paddingValues = innerPadding,
                    navigateToHome = appState::navigateToHome
                )

                homeNavGraph(
                    paddingValues = innerPadding,
                    navigateToDiaryFeedback = appState::navigateToDiaryFeedback,
                    navigateToDiaryWrite = appState::navigateToDiaryWrite,
                    navigateToNotification = appState::navigateToNotification,
                    navigateToFeedProfile = appState::navigateToFeedProfile,
                    navigateToFeed = appState::navigateToFeed
                )

                notificationNavGraph(
                    paddingValues = innerPadding,
                    navController = appState.navController,
                    navigateUp = appState::navigateUp,
                    navigateToFeedDiary = appState::navigateToFeedDiary,
                    navigateToFeedProfile = appState::navigateToFeedProfile
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
                    navigateUp = appState::navigateUp,
                    navigateToHome = appState::navigateToHome,
                    navigateToFeed = appState::navigateToFeed,
                    navigateToVoca = appState::navigateToVoca
                )

                feedNavGraph(
                    paddingValues = innerPadding,
                    navController = appState.navController,
                    navigateToFeedDiary = appState::navigateToFeedDiary,
                    navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                    navigateToFeedProfile = appState::navigateToFeedProfile
                )

                feedDiaryNavGraph(
                    paddingValues = innerPadding,
                    navigateUp = appState::navigateUp,
                    navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                    navigateToFeedProfile = appState::navigateToFeedProfile,
                    navigateToVoca = appState::navigateToVoca
                )

                myPageNavGraph(
                    paddingValues = innerPadding,
                    navController = appState.navController,
                    navigateUp = appState::navigateUp,
                    navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                    navigateToFeedProfile = appState::navigateToFeedProfile,
                    navigateToAlarm = appState::navigateToNotificationSetting
                )

                feedProfileNavGraph(
                    paddingValues = innerPadding,
                    navigateUp = appState::navigateUp,
                    navigateToFeedProfile = appState::navigateToFeedProfile,
                    navController = appState.navController,
                    navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                    navigateToFeedDiary = appState::navigateToFeedDiary
                )
            }

            HilingualErrorDialog(
                state = appState.dialogStateHolder.dialogState,
                onDismiss = appState.dialogStateHolder::dismissDialog
            )

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(bottom = 82.dp)
            ) {
                SnackbarHost(hostState = snackBarHostState) { data ->
                    when (val visuals = data.visuals) {
                        is Snackbar -> {
                            HilingualActionSnackbar(
                                message = visuals,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                onDismiss = { data.dismiss() }
                            )
                        }

                        is Toast -> {
                            TextToast(text = visuals.message)
                        }

                        else -> {
                            TextToast(text = visuals.message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    onShowMessage: () -> Unit = {}
) {
    val context = LocalActivity.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= MessageDuration.DEFAULT.millis) {
            context?.finish()
        } else {
            onShowMessage()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
