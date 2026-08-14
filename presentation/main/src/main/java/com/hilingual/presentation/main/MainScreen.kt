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

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.angrypodo.wisp.runtime.navigateTo
import com.hilingual.core.ads.native.HilingualNativeLineAd
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.app.AppRestarter
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.model.HilingualMessage.Snackbar
import com.hilingual.core.common.model.HilingualMessage.Toast
import com.hilingual.core.common.model.MessageDuration
import com.hilingual.core.common.provider.LocalAppRestarter
import com.hilingual.core.common.provider.LocalIsOffline
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.DialogType
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.trigger.LocalReconnectEvents
import com.hilingual.core.common.trigger.rememberDialogTrigger
import com.hilingual.core.designsystem.component.dialog.HilingualErrorDialog
import com.hilingual.core.designsystem.component.snackbar.HilingualActionSnackbar
import com.hilingual.core.designsystem.component.toast.TextToast
import com.hilingual.core.designsystem.component.view.HilingualNetworkErrorView
import com.hilingual.presentation.auth.navigation.Auth
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
import com.hilingual.presentation.onboarding.navigation.Onboarding
import com.hilingual.presentation.onboarding.navigation.onboardingNavGraph
import com.hilingual.presentation.signup.navigation.SignUp
import com.hilingual.presentation.signup.navigation.signUpGraph
import com.hilingual.presentation.splash.navigation.Splash
import com.hilingual.presentation.splash.navigation.splashNavGraph
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber

@Composable
internal fun MainScreen(
    appState: MainAppState,
    tracker: Tracker,
    appRestarter: AppRestarter,
    deepLinkUri: Uri? = null,
    onDeepLinkConsumed: () -> Unit,
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isBottomBarVisible by appState.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()
    val currentBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }
    val canShowNetworkError = currentBackStackEntry?.destination?.canShowNetworkError() == true

    var isNetworkErrorOverlayVisible by remember { mutableStateOf(false) }
    var pendingDialogRequest by remember { mutableStateOf<PendingDialogRequest?>(null) }
    val dialogTrigger = rememberDialogTrigger(
        show = { type, onClick ->
            val request = PendingDialogRequest(
                backStackEntry = currentBackStackEntry,
                type = type,
                onClick = onClick,
            )
            if (isNetworkErrorOverlayVisible) {
                pendingDialogRequest = request
            } else {
                appState.dialogStateHolder.showDialog(type, onClick)
            }
        },
    )
    val reconnectEvents = remember { MutableSharedFlow<Unit>() }

    suspend fun retryAndDismissNetworkError() {
        reconnectEvents.emit(Unit)
        // Give retry callbacks a chance to publish Loading without depending on the UI frame clock.
        yield()
        if (!appState.isOffline.value) {
            isNetworkErrorOverlayVisible = false
        }
    }

    val onShowMessage: (HilingualMessage) -> Unit =
        remember(
            key1 = coroutineScope,
            key2 = snackBarHostState,
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

    LaunchedEffect(currentBackStackEntry) {
        isNetworkErrorOverlayVisible = isOffline && canShowNetworkError
    }

    LaunchedEffect(Unit) {
        appState.isOffline
            .drop(1)
            .filter { offline -> !offline }
            .collect {
                retryAndDismissNetworkError()
            }
    }

    LaunchedEffect(isOffline, canShowNetworkError) {
        if (isOffline && canShowNetworkError) {
            onShowMessage(Toast("인터넷 연결이 불안정해요."))
        }
    }

    LaunchedEffect(isNetworkErrorOverlayVisible, currentBackStackEntry) {
        pendingDialogRequest = pendingDialogRequest
            ?.takeIf { it.backStackEntry == currentBackStackEntry }

        if (isNetworkErrorOverlayVisible) {
            appState.dialogStateHolder.dismissDialog()
        } else {
            pendingDialogRequest?.let { request ->
                pendingDialogRequest = null
                // Retryable errors are replaced by the reconnect request; terminal errors still need user action.
                if (request.type == DialogType.NOT_FOUND) {
                    appState.dialogStateHolder.showDialog(request.type, request.onClick)
                }
            }
        }
    }

    HandleBackPressToExit(
        onShowMessage = { onShowMessage(Toast("버튼을 한번 더 누르면 앱이 종료됩니다.")) },
    )

    CompositionLocalProvider(
        LocalIsOffline provides isOffline,
        LocalDialogTrigger provides dialogTrigger,
        LocalMessageController provides onShowMessage,
        LocalReconnectEvents provides reconnectEvents,
        LocalTracker provides tracker,
        LocalAppRestarter provides appRestarter,
    ) {
        Scaffold(
            bottomBar = {
                BottomSection(
                    isVisible = isBottomBarVisible,
                    isInteractionEnabled = !isNetworkErrorOverlayVisible,
                    currentTab = currentTab,
                    onTabSelected = appState::navigate,
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None },
                    navController = appState.navController,
                    startDestination = appState.startDestination,
                ) {
                    splashNavGraph(
                        navigateToAuth = appState::navigateToAuth,
                        navigateToHome = appState::navigateToHome,
                        navigateToOnboarding = appState::navigateToOnboarding,
                    )

                    authNavGraph(
                        paddingValues = innerPadding,
                        navigateToHome = appState::navigateToHome,
                        navigateToSignUp = appState::navigateToSignUp,
                    )

                    signUpGraph(
                        paddingValues = innerPadding,
                        navigateToHome = appState::navigateToHome,
                    )

                    homeNavGraph(
                        paddingValues = innerPadding,
                        navigateToDiaryFeedback = appState::navigateToDiaryFeedback,
                        navigateToDiaryWrite = appState::navigateToDiaryWrite,
                        navigateToNotification = appState::navigateToNotification,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
                        navigateToFeed = appState::navigateToFeed,
                    )

                    notificationNavGraph(
                        paddingValues = innerPadding,
                        navController = appState.navController,
                        navigateUp = appState::navigateUp,
                        navigateToFeedDiary = appState::navigateToFeedDiary,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
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
                                },
                            )
                        },
                    )

                    vocaNavGraph(
                        paddingValues = innerPadding,
                        navController = appState.navController,
                        navigateToHome = appState::navigateToHome,
                    )

                    diaryFeedbackNavGraph(
                        paddingValues = innerPadding,
                        navigateUp = appState::navigateUp,
                        navigateToHome = appState::navigateToHome,
                        navigateToFeed = appState::navigateToFeed,
                        navigateToVoca = appState::navigateToVoca,
                    )

                    feedNavGraph(
                        paddingValues = innerPadding,
                        navController = appState.navController,
                        navigateToFeedDiary = appState::navigateToFeedDiary,
                        navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
                    )

                    feedDiaryNavGraph(
                        paddingValues = innerPadding,
                        navigateUp = appState::navigateUp,
                        navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
                        navigateToVoca = appState::navigateToVoca,
                    )

                    myPageNavGraph(
                        paddingValues = innerPadding,
                        navController = appState.navController,
                        navigateUp = appState::navigateUp,
                        navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
                        navigateToAlarm = appState::navigateToNotificationSetting,
                    )

                    feedProfileNavGraph(
                        paddingValues = innerPadding,
                        navigateUp = appState::navigateUp,
                        navigateToFeedProfile = appState::navigateToFeedProfile,
                        navController = appState.navController,
                        navigateToMyFeedProfile = appState::navigateToMyFeedProfile,
                        navigateToFeedDiary = appState::navigateToFeedDiary,
                    )

                    onboardingNavGraph(
                        paddingValues = innerPadding,
                        navigateToAuth = appState::navigateToAuth,
                    )
                }

                LaunchedEffect(appState.navController, deepLinkUri) {
                    if (deepLinkUri == null) return@LaunchedEffect

                    val entry = appState.navController.currentBackStackEntryFlow
                        .first { !it.destination.hasRoute(Splash::class) }

                    val isPreLogin = entry.destination.run {
                        hasRoute(Auth::class) || hasRoute(SignUp::class) || hasRoute(Onboarding::class)
                    }

                    if (isPreLogin) {
                        onDeepLinkConsumed()
                        return@LaunchedEffect
                    }

                    try {
                        appState.navController.navigateTo(deepLinkUri)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to navigate to deep link: $deepLinkUri")
                        onShowMessage(Toast("연결할 수 없는 링크예요."))
                    }
                    onDeepLinkConsumed()
                }

                HilingualErrorDialog(
                    state = appState.dialogStateHolder.dialogState,
                    onDismiss = appState.dialogStateHolder::dismissDialog,
                )

                if (isNetworkErrorOverlayVisible) {
                    HilingualNetworkErrorView(
                        isBackVisible = !isBottomBarVisible,
                        onBackClick = appState::navigateUp,
                        onRetryClick = {
                            if (isOffline) {
                                onShowMessage(Toast("인터넷 연결이 불안정해요."))
                            } else {
                                coroutineScope.launch {
                                    retryAndDismissNetworkError()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    )
                }

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(bottom = 106.dp),
                ) {
                    SnackbarHost(hostState = snackBarHostState) { data ->
                        when (val visuals = data.visuals) {
                            is Snackbar -> {
                                HilingualActionSnackbar(
                                    message = visuals,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    onDismiss = { data.dismiss() },
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
}

private data class PendingDialogRequest(
    val backStackEntry: NavBackStackEntry?,
    val type: DialogType,
    val onClick: () -> Unit,
)

private fun NavDestination.canShowNetworkError(): Boolean =
    !hasRoute(Splash::class) && !hasRoute(Auth::class) && !hasRoute(Onboarding::class) && !hasRoute(SignUp::class)

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    onShowMessage: () -> Unit = {},
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

@Composable
private fun BottomSection(
    isVisible: Boolean,
    isInteractionEnabled: Boolean,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideIn { IntOffset(0, it.height) },
            exit = fadeOut() + slideOut { IntOffset(0, it.height) },
        ) {
            MainBottomBar(
                tabs = MainTab.entries.toPersistentList(),
                currentTab = currentTab,
                onTabSelected = onTabSelected,
            )
        }
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.navigationBarsPadding(),
            enter = EnterTransition.None,
            exit = ExitTransition.None,
        ) {
            Box {
                HilingualNativeLineAd(adUnitId = BuildConfig.ADMOB_NATIVE_UNIT_ID)
                if (!isInteractionEnabled) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .noRippleClickable {},
                    )
                }
            }
        }
    }
}
