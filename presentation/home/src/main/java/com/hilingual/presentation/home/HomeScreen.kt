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
package com.hilingual.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.ads.rewarded.showRewardedAd
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page.HOME
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.DialogState
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.view.HilingualLoadErrorView
import com.hilingual.core.designsystem.component.view.LoadErrorViewAction
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.navigation.DiaryWriteMode
import com.hilingual.data.calendar.model.CalendarStatus
import com.hilingual.presentation.home.component.HomeHeader
import com.hilingual.presentation.home.component.calendar.HilingualCalendar
import com.hilingual.presentation.home.component.dialog.DiaryContinueDialog
import com.hilingual.presentation.home.component.dialog.NotificationDialog
import com.hilingual.presentation.home.component.dialog.RecoveryNoticeModal
import com.hilingual.presentation.home.component.dialog.RecoveryReminderModal
import com.hilingual.presentation.home.component.footer.DiaryDateInfo
import com.hilingual.presentation.home.component.footer.DiaryEmptyCard
import com.hilingual.presentation.home.component.footer.DiaryEmptyCardType
import com.hilingual.presentation.home.component.footer.DiaryPreviewCard
import com.hilingual.presentation.home.component.footer.DiaryTimeInfo
import com.hilingual.presentation.home.component.footer.HomeDropDownMenu
import com.hilingual.presentation.home.component.footer.RecoveryButton
import com.hilingual.presentation.home.component.footer.RecoveryGuideCard
import com.hilingual.presentation.home.component.footer.TodayTopic
import com.hilingual.presentation.home.component.footer.WriteDiaryButton
import com.hilingual.presentation.home.component.onboarding.HomeOnboardingBottomSheet
import com.hilingual.presentation.home.component.onboarding.HomeOnboardingContent
import com.hilingual.presentation.home.type.DiaryCardState
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToDiaryWrite: (selectedDate: LocalDate, mode: DiaryWriteMode) -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    navigateToNotification: () -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit,
    navigateToFeed: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    homeState: HomeState = rememberHomeState(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current
    val messageController = LocalMessageController.current
    val tracker = LocalTracker.current
    val context = LocalContext.current
    val activity = LocalActivity.current
    val isSuccess = uiState is UiState.Success

    if (homeState.isErrorDialogVisible) {
        dialogTrigger.show(
            onClick = {
                homeState.onErrorRetry?.invoke()
                homeState.hideErrorDialog()
            },
        )
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.ShowErrorDialog -> homeState.showErrorDialog(sideEffect.onRetry)

            is HomeSideEffect.ShowToast -> messageController(HilingualMessage.Toast(sideEffect.text))

            is HomeSideEffect.ShowSnackBar -> {
                messageController(
                    HilingualMessage.Snackbar(
                        message = sideEffect.message,
                        actionLabelText = sideEffect.actionLabel,
                        onAction = navigateToFeed,
                    ),
                )
            }

            is HomeSideEffect.ShowOnboarding -> {
                homeState.showOnboardingBottomSheet()
            }

            is HomeSideEffect.ShowNotificationDialog -> {
                homeState.showNotificationDialog()
            }

            is HomeSideEffect.ShowRewardedAd -> {
                if (activity != null) {
                    homeState.showRecoveryAdLoading()
                    showRewardedAd(
                        activity = activity,
                        adUnitId = BuildConfig.ADMOB_STREAKREWARD_UNIT_ID,
                        onRewardEarned = {
                            homeState.hideRecoveryAdLoading()
                            viewModel.onRewardEarned(sideEffect.date)
                        },
                        onAdDismissed = {
                            homeState.hideRecoveryAdLoading()
                            viewModel.onRecoveryAdFinished()
                        },
                        onAdFailedToLoad = {
                            homeState.hideRecoveryAdLoading()
                            viewModel.onRecoveryAdFinished()
                            messageController(HilingualMessage.Toast("광고를 불러오지 못했어요.\n잠시 후 다시 시도해주세요."))
                        },
                    )
                } else {
                    viewModel.onRecoveryAdFinished()
                }
            }

            is HomeSideEffect.NavigateToRecoveryWrite ->
                navigateToDiaryWrite(sideEffect.date, DiaryWriteMode.RECOVERY)

            is HomeSideEffect.ShowRecoveryNotice -> homeState.showRecoveryNotice()

            is HomeSideEffect.ShowRecoveryReminder -> homeState.showRecoveryReminder()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
        tracker.logEvent(trigger = TriggerType.VIEW, page = HOME, event = "page")
    }

    if (ENABLE_PUSH_NOTIFICATION) {
        CheckNotificationPermission(
            context = context,
            isDataLoaded = isSuccess,
            onCheck = viewModel::handleNotificationPermission,
        )
    }

    NotificationDialog(
        state = DialogState(isVisible = homeState.isNotificationDialogVisible),
        onDismiss = {
            homeState.hideNotificationDialog()
            viewModel.onNotificationDialogDismissed()
        },
        onConfirm = {
            homeState.hideNotificationDialog()
            viewModel.onNotificationDialogDismissed()
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        },
    )

    if (ENABLE_RECOVERY_NOTICE) {
        RecoveryNoticeModal(
            isVisible = homeState.isRecoveryNoticeVisible,
            onClick = {
                homeState.hideRecoveryNotice()
                viewModel.onRecoveryNoticeConfirmed()
            },
            onDismiss = {
                homeState.hideRecoveryNotice()
                viewModel.onRecoveryNoticeConfirmed()
            },
        )
    }

    RecoveryReminderModal(
        isVisible = homeState.isRecoveryReminderVisible,
        onClick = {
            homeState.hideRecoveryReminder()
            viewModel.onRecoveryReminderConfirmed()
        },
        onLaterClick = {
            homeState.hideRecoveryReminder()
            viewModel.onRecoveryReminderLater()
        },
        onDismiss = {
            homeState.hideRecoveryReminder()
        },
    )

    when (val state = uiState) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            HomeScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                homeState = homeState,
                onAlarmClick = navigateToNotification,
                onImageClick = {
                    tracker.logEvent(trigger = TriggerType.CLICK, page = HOME, event = "profile")
                    navigateToFeedProfile(0L)
                },
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onRecoveryClick = viewModel::onRecoveryClick,
                onWriteDiaryClick = { date, mode ->
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = HOME,
                        event = "diary_write",
                        properties = mapOf("open_time" to System.currentTimeMillis()),
                    )
                    navigateToDiaryWrite(date, mode)
                },
                onDiaryPreviewClick = { diaryId ->
                    tracker.logEvent(
                        trigger = TriggerType.VIEW,
                        page = HOME,
                        event = "opend_diary_view",
                        properties = mapOf(
                            "open_time" to System.currentTimeMillis(),
                            "entry_id" to diaryId,
                        ),
                    )
                    navigateToDiaryFeedback(diaryId)
                },
                onDeleteClick = { /* viewModel::deleteDiary 수정기능 도입까지 삭제 기능 지원중단 */ },
                onPublishClick = viewModel::publishDiary,
                onUnpublishClick = viewModel::unpublishDiary,
                tracker = tracker,
            )
        }

        is UiState.Failure -> {
            HilingualLoadErrorView(
                action = LoadErrorViewAction.Retry(
                    onRetryClick = viewModel::loadInitialData,
                ),
                modifier = Modifier.padding(paddingValues),
            )
        }

        else -> {}
    }

    fun handleOnboardingDismiss() {
        homeState.hideOnboardingBottomSheet()
        if (ENABLE_PUSH_NOTIFICATION) {
            viewModel.onNotificationPermissionAfterOnboarding(
                isGranted = context.isNotificationPermissionGranted(),
                requiresPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
            )
        }
    }

    HomeOnboardingBottomSheet(
        isVisible = homeState.isOnboardingBottomSheetVisible,
        onCloseButtonClick = ::handleOnboardingDismiss,
    ) {
        HomeOnboardingContent(
            onStartButtonClick = ::handleOnboardingDismiss,
        )
    }

    if (homeState.isRecoveryAdLoading) {
        HilingualLoadingIndicator(
            backgroundColor = Color.Black.copy(alpha = 0.32f),
        )
    }
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    uiState: HomeUiState,
    homeState: HomeState,
    onAlarmClick: () -> Unit,
    onImageClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onRecoveryClick: (LocalDate) -> Unit,
    onWriteDiaryClick: (selectedDate: LocalDate, mode: DiaryWriteMode) -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit,
    onDeleteClick: (diaryId: Long) -> Unit,
    onPublishClick: (diaryId: Long) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    tracker: Tracker,
) {
    val date = uiState.calendar.selectedDate

    DiaryContinueDialog(
        isVisible = homeState.isDiaryContinueDialogVisible,
        onDismiss = homeState::hideDiaryContinueDialog,
        onNewClick = {
            onWriteDiaryClick(date, DiaryWriteMode.NEW)
            homeState.hideDiaryContinueDialog()
        },
        onContinueClick = {
            onWriteDiaryClick(date, DiaryWriteMode.DEFAULT)
            homeState.hideDiaryContinueDialog()
        },
    )

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .statusBarColor(hilingualBlack)
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(homeState.scrollState),
    ) {
        with(uiState.header) {
            HomeHeader(
                imageUrl = userProfile.profileImg,
                nickname = userProfile.nickname,
                totalDiaries = userProfile.totalDiaries,
                streak = userProfile.streak,
                isNewAlarm = userProfile.isNewAlarm,
                count = userProfile.recoveryTickets,
                onAlarmClick = onAlarmClick,
                onImageClick = onImageClick,
                modifier = Modifier
                    .background(hilingualBlack)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 12.dp),
            )
        }

        with(uiState.calendar) {
            HilingualCalendar(
                selectedDate = selectedDate,
                writtenDates = dates
                    .filter { it.status != CalendarStatus.UNLOCKED }
                    .map { it.date }
                    .toSet(),
                onDateClick = onDateSelected,
                onMonthChanged = onMonthChanged,
                modifier = Modifier
                    .background(HilingualTheme.colors.hilingualBlack)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(white)
                    .padding(16.dp)
                    .animateContentSize(),
            )
        }

        HorizontalDivider(
            thickness = 4.dp,
            color = HilingualTheme.colors.gray100,
        )

        Column(
            modifier = Modifier
                .background(HilingualTheme.colors.white)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            val contentState = uiState.diaryContent

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DiaryDateInfo(
                    selectedDate = date,
                    isPublished = contentState.diaryThumbnail?.isPublished ?: false,
                    isWritten = contentState.cardState == DiaryCardState.WRITTEN,
                    modifier = Modifier.heightIn(min = 20.dp),
                )

                when (contentState.cardState) {
                    DiaryCardState.WRITTEN -> {
                        contentState.diaryThumbnail?.let { diary ->
                            HomeDropDownMenu(
                                isExpanded = homeState.isMoreMenuExpanded,
                                isPublished = diary.isPublished,
                                onExpandedChange = { isExpanded ->
                                    if (isExpanded) {
                                        homeState.showMoreMenu()
                                        tracker.logEvent(
                                            trigger = TriggerType.CLICK,
                                            page = HOME,
                                            event = "more_menu",
                                            properties = mapOf("menu_name" to "more_menu"),
                                        )
                                    } else {
                                        homeState.hideMoreMenu()
                                    }
                                },
                                onDeleteClick = { /* onDeleteClick(diary.diaryId) 수정기능 도입까지 삭제 기능 지원중단 */ },
                                onPublishClick = { onPublishClick(diary.diaryId) },
                                onUnpublishClick = { onUnpublishClick(diary.diaryId) },
                            )
                        }
                    }

                    DiaryCardState.WRITABLE -> {
                        DiaryTimeInfo(remainingTime = contentState.todayTopic?.remainingTime)
                    }

                    else -> {}
                }
            }

            Spacer(Modifier.height(16.dp))

            with(contentState) {
                when (cardState) {
                    DiaryCardState.WRITTEN -> {
                        if (diaryThumbnail != null) {
                            DiaryPreviewCard(
                                diaryText = diaryThumbnail.originalText,
                                diaryId = diaryThumbnail.diaryId,
                                onClick = onDiaryPreviewClick,
                                imageUrl = diaryThumbnail.imageUrl,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    DiaryCardState.FUTURE -> DiaryEmptyCard(type = DiaryEmptyCardType.FUTURE)

                    DiaryCardState.WRITABLE -> {
                        if (todayTopic != null) {
                            TodayTopic(
                                koTopic = todayTopic.topicKo,
                                enTopic = todayTopic.topicEn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize()
                                    .noRippleClickable {
                                        tracker.logEvent(
                                            trigger = TriggerType.CLICK,
                                            page = HOME,
                                            event = "switch_language",
                                            properties = mapOf(
                                                "recommen_topic" to "${todayTopic.topicKo}/${todayTopic.topicEn}",
                                            ),
                                        )
                                    },
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        WriteDiaryButton(
                            onClick = {
                                if (contentState.isDiaryTempExist) {
                                    homeState.showDiaryContinueDialog()
                                } else {
                                    onWriteDiaryClick(date, DiaryWriteMode.DEFAULT)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    DiaryCardState.UNLOCKED -> {
                        if (todayTopic != null) {
                            TodayTopic(
                                koTopic = todayTopic.topicKo,
                                enTopic = todayTopic.topicEn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize(),
                                isRecovery = true,
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        WriteDiaryButton(
                            onClick = { onWriteDiaryClick(date, DiaryWriteMode.RECOVERY) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    DiaryCardState.RECOVERABLE -> {
                        RecoveryGuideCard(modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(12.dp))
                        RecoveryButton(
                            onClick = { onRecoveryClick(date) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    DiaryCardState.RECOVERY_EXHAUSTED ->
                        DiaryEmptyCard(type = DiaryEmptyCardType.RECOVERY_EXHAUSTED)

                    DiaryCardState.REWRITE_DISABLED,
                    DiaryCardState.PAST,
                    -> DiaryEmptyCard(type = DiaryEmptyCardType.PAST)
                }
            }
        }
    }
}

// #807 푸시 알림 플로우: 미배포 기능, 당분간 봉인. 재개 시 true로 전환.
private const val ENABLE_PUSH_NOTIFICATION = true

private const val ENABLE_RECOVERY_NOTICE = false

@Composable
private fun CheckNotificationPermission(
    context: Context,
    isDataLoaded: Boolean,
    onCheck: (isGranted: Boolean, requiresPermission: Boolean) -> Unit,
) {
    val requiresPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun checkPermission() {
        onCheck(context.isNotificationPermissionGranted(), requiresPermission)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (isDataLoaded) checkPermission()
    }

    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) checkPermission()
    }
}

private fun Context.isNotificationPermissionGranted(): Boolean =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED

        else -> true
    }

@Preview
@Composable
private fun HomeScreenPreview() {
    HilingualTheme {
        HomeScreen(
            paddingValues = PaddingValues(),
            uiState = HomeUiState.Fake,
            homeState = rememberHomeState(),
            onAlarmClick = {},
            onImageClick = {},
            onDateSelected = {},
            onMonthChanged = {},
            onRecoveryClick = {},
            onWriteDiaryClick = { _, _ -> },
            onDiaryPreviewClick = {},
            onDeleteClick = {},
            onPublishClick = {},
            onUnpublishClick = {},
            tracker = FakeTracker(),
        )
    }
}
