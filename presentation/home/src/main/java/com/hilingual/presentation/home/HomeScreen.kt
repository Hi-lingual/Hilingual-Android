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
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page.HOME
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.navigation.DiaryWriteMode
import com.hilingual.presentation.home.component.DiaryContinueDialog
import com.hilingual.presentation.home.component.HomeHeader
import com.hilingual.presentation.home.component.calendar.HilingualCalendar
import com.hilingual.presentation.home.component.footer.DiaryDateInfo
import com.hilingual.presentation.home.component.footer.DiaryEmptyCard
import com.hilingual.presentation.home.component.footer.DiaryEmptyCardType
import com.hilingual.presentation.home.component.footer.DiaryPreviewCard
import com.hilingual.presentation.home.component.footer.DiaryTimeInfo
import com.hilingual.presentation.home.component.footer.HomeDropDownMenu
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
    homeState: HomeState = rememberHomeState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current
    val messageController = LocalMessageController.current
    val tracker = LocalTracker.current
    val context = LocalContext.current
    val isSuccess = uiState is UiState.Success

    var isOnboardingVisible by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onNotificationPermissionResult(isGranted = isGranted)
    }

    if (homeState.isErrorDialogVisible) {
        dialogTrigger.show(
            onClick = {
                homeState.onErrorRetry?.invoke()
                homeState.hideErrorDialog()
            }
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
                        onAction = navigateToFeed
                    )
                )
            }

            is HomeSideEffect.RequestNotificationPermission -> {
                notificationPermissionLauncher.launch(sideEffect.permission)
            }

            is HomeSideEffect.ShowOnboarding -> {
                isOnboardingVisible = true
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
        tracker.logEvent(trigger = TriggerType.VIEW, page = HOME, event = "page")
    }

    CheckNotificationPermission(
        context = context,
        isDataLoaded = isSuccess,
        onCheck = viewModel::handleNotificationPermission
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
                onWriteDiaryClick = { date, mode ->
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = HOME,
                        event = "diary_write",
                        properties = mapOf("open_time" to System.currentTimeMillis())
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
                            "entry_id" to diaryId
                        )
                    )
                    navigateToDiaryFeedback(diaryId)
                },
                onDeleteClick = viewModel::deleteDiary,
                onPublishClick = viewModel::publishDiary,
                onUnpublishClick = viewModel::unpublishDiary,
                tracker = tracker
            )
        }

        else -> {}
    }

    HomeOnboardingBottomSheet(
        isVisible = isOnboardingVisible,
        onCloseButtonClick = { isOnboardingVisible = false }
    ) {
        HomeOnboardingContent(
            onStartButtonClick = { isOnboardingVisible = false }
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
    onWriteDiaryClick: (selectedDate: LocalDate, mode: DiaryWriteMode) -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit,
    onDeleteClick: (diaryId: Long) -> Unit,
    onPublishClick: (diaryId: Long) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    tracker: Tracker
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
        }
    )

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .statusBarColor(hilingualBlack)
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(homeState.scrollState)
    ) {
        with(uiState.header) {
            HomeHeader(
                imageUrl = userProfile.profileImg,
                nickname = userProfile.nickname,
                totalDiaries = userProfile.totalDiaries,
                streak = userProfile.streak,
                isNewAlarm = userProfile.isNewAlarm,
                onAlarmClick = onAlarmClick,
                onImageClick = onImageClick,
                modifier = Modifier
                    .background(hilingualBlack)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 12.dp)
            )
        }

        with(uiState.calendar) {
            HilingualCalendar(
                selectedDate = selectedDate,
                writtenDates = dates.map { it.date }.toSet(),
                onDateClick = onDateSelected,
                onMonthChanged = onMonthChanged,
                modifier = Modifier
                    .background(HilingualTheme.colors.hilingualBlack)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(white)
                    .padding(16.dp)
                    .animateContentSize()
            )
        }

        HorizontalDivider(
            thickness = 4.dp,
            color = HilingualTheme.colors.gray100
        )

        Column(
            modifier = Modifier
                .background(HilingualTheme.colors.white)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val contentState = uiState.diaryContent

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DiaryDateInfo(
                    selectedDate = date,
                    isPublished = contentState.diaryThumbnail?.isPublished ?: false,
                    isWritten = contentState.cardState == DiaryCardState.WRITTEN,
                    modifier = Modifier.heightIn(min = 20.dp)
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
                                            properties = mapOf("menu_name" to "more_menu")
                                        )
                                    } else {
                                        homeState.hideMoreMenu()
                                    }
                                },
                                onDeleteClick = { onDeleteClick(diary.diaryId) },
                                onPublishClick = { onPublishClick(diary.diaryId) },
                                onUnpublishClick = { onUnpublishClick(diary.diaryId) }
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
                                modifier = Modifier.fillMaxWidth()
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
                                                "recommen_topic" to "${todayTopic.topicKo}/${todayTopic.topicEn}"
                                            )
                                        )
                                    }
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
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DiaryCardState.REWRITE_DISABLED,
                    DiaryCardState.PAST -> DiaryEmptyCard(type = DiaryEmptyCardType.PAST)
                }
            }
        }
    }
}

@Composable
private fun CheckNotificationPermission(
    context: Context,
    isDataLoaded: Boolean,
    onCheck: (isGranted: Boolean, requiresPermission: Boolean) -> Unit
) {
    val requiresPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun checkPermission() {
        val isGranted = when {
            requiresPermission -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            }

            else -> true
        }
        onCheck(isGranted, requiresPermission)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        checkPermission()
    }

    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) checkPermission()
    }
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
            onWriteDiaryClick = { _, _ -> },
            onDiaryPreviewClick = {},
            onDeleteClick = {},
            onPublishClick = {},
            onUnpublishClick = {},
            tracker = FakeTracker()
        )
    }
}
