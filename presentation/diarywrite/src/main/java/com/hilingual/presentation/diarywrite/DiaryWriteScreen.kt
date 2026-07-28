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
package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page.AI_FEEDBACK
import com.hilingual.core.common.analytics.Page.WRITE_DIARY
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.advancedImePadding
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.extension.subScreenPadding
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.common.util.toKoreanFullDate
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualLongTextField
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.presentation.diarywrite.component.DiaryOverwriteDialog
import com.hilingual.presentation.diarywrite.component.DiaryWriteCancelBottomSheet
import com.hilingual.presentation.diarywrite.component.ImageSelectBottomSheet
import com.hilingual.presentation.diarywrite.component.PhotoSelectButton
import com.hilingual.presentation.diarywrite.component.RecommendedTopicDropdown
import com.hilingual.presentation.diarywrite.component.TextScanButton
import com.hilingual.presentation.diarywrite.component.WriteGuideTooltip
import com.hilingual.presentation.diarywrite.screen.DiaryCompleteScreen
import com.hilingual.presentation.diarywrite.screen.DiaryFailureScreen
import com.hilingual.presentation.diarywrite.screen.DiaryFeedbackLoadingScreen
import com.hilingual.presentation.diarywrite.screen.rememberFeedbackLoadingLottieCompositions
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.rememberBalloonState
import com.skydoves.balloon.compose.setBackgroundColor
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@Composable
internal fun DiaryWriteRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    viewModel: DiaryWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val feedbackUiState by viewModel.feedbackUiState.collectAsStateWithLifecycle()

    val dialogTrigger = LocalDialogTrigger.current
    val messageController = LocalMessageController.current
    val tracker = LocalTracker.current

    val lottieCompositions = rememberFeedbackLoadingLottieCompositions()
    val textScanState = rememberTextScanState(onImageSelected = viewModel::extractTextFromImage)

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DiaryWriteSideEffect.NavigateToHome -> navigateToHome()
            is DiaryWriteSideEffect.ShowErrorDialog -> dialogTrigger.show(onClick = navigateUp)
            is DiaryWriteSideEffect.ShowToast -> messageController(HilingualMessage.Toast(sideEffect.message))
        }
    }

    LaunchedEffect(Unit) {
        tracker.logEvent(trigger = TriggerType.VIEW, page = WRITE_DIARY, event = "page")
    }

    when (val feedbackState = feedbackUiState) {
        is UiState.Empty -> {
            DiaryWriteScreen(
                paddingValues = paddingValues,
                uiState = uiState,
                onBackClick = navigateUp,
                onTempSaveClick = viewModel::saveDiaryTemp,
                onDiaryTextChange = viewModel::updateDiaryText,
                onDiaryImageUriChange = viewModel::updateDiaryImageUri,
                onCameraClick = textScanState.launchCamera,
                onGalleryClick = textScanState.launchGallery,
                onFeedbackRequestClick = {
                    if (viewModel.requestDiaryFeedback()) {
                        tracker.logEvent(
                            trigger = TriggerType.CLICK,
                            page = WRITE_DIARY,
                            event = "submit_cta",
                            properties = mapOf(
                                "has_photo" to (uiState.diaryImageUri != null),
                                "char_count" to uiState.diaryText.length,
                            ),
                        )
                    }
                },
                tracker = tracker,
            )
        }

        is UiState.Loading -> {
            DiaryFeedbackLoadingScreen(
                lottieCompositions = lottieCompositions,
                paddingValues = paddingValues,
            )
        }

        is UiState.Success -> {
            DiaryCompleteScreen(
                paddingValues = paddingValues,
                diaryId = feedbackState.data,
                onCloseButtonClick = navigateToHome,
                onShowFeedbackButtonClick = navigateToDiaryFeedback,
            )
        }

        is UiState.Failure -> {
            BackHandler(onBack = viewModel::returnToWriting)

            DiaryFailureScreen(
                paddingValues = paddingValues,
                onBackClick = viewModel::returnToWriting,
                onRequestAgainButtonClick = {
                    if (viewModel.requestDiaryFeedback()) {
                        tracker.logEvent(
                            trigger = TriggerType.CLICK,
                            page = AI_FEEDBACK,
                            event = "feedback_retry",
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun DiaryWriteScreen(
    paddingValues: PaddingValues,
    uiState: DiaryWriteUiState,
    onBackClick: () -> Unit,
    onTempSaveClick: () -> Unit,
    onDiaryTextChange: (String) -> Unit,
    onDiaryImageUriChange: (Uri?) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onFeedbackRequestClick: () -> Unit,
    tracker: Tracker,
) {
    val focusManager = LocalFocusManager.current
    val verticalScrollState = rememberScrollState()

    var isCancelBottomSheetVisible by remember { mutableStateOf(false) }
    var isOverwriteDialogVisible by remember { mutableStateOf(false) }
    var isImageBottomSheetVisible by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    var dropdownClickCount by remember { mutableIntStateOf(0) }
    var textFieldFocusedTime by remember { mutableLongStateOf(0L) }

    val isFeedbackRequestEnabled = uiState.diaryText.length >= MIN_FEEDBACK_REQUEST_LENGTH

    val requestExit = {
        if (uiState.hasUnsavedChanges) {
            isCancelBottomSheetVisible = true
        } else {
            onBackClick()
        }
    }

    BackHandler(onBack = requestExit)

    DiaryWriteCancelBottomSheet(
        isVisible = isCancelBottomSheetVisible,
        onDismiss = { isCancelBottomSheetVisible = false },
        onCancelClick = {
            tracker.logEvent(
                trigger = TriggerType.CLICK,
                page = WRITE_DIARY,
                event = "modal",
                properties = mapOf("modal_action" to "confirm_exit"),
            )
            onBackClick()
        },
        onTempSaveClick = {
            if (uiState.isDiaryTempExist) {
                isCancelBottomSheetVisible = false
                isOverwriteDialogVisible = true
            } else {
                onTempSaveClick()
            }
        },
    )

    DiaryOverwriteDialog(
        isVisible = isOverwriteDialogVisible,
        onDismiss = { isOverwriteDialogVisible = false },
        onNoClick = { isOverwriteDialogVisible = false },
        onOverwriteClick = onTempSaveClick,
    )

    ImageSelectBottomSheet(
        isVisible = isImageBottomSheetVisible,
        onDismiss = { isImageBottomSheetVisible = false },
        onCameraSelected = {
            onCameraClick()
            isImageBottomSheetVisible = false
        },
        onGallerySelected = {
            onGalleryClick()
            isImageBottomSheetVisible = false
        },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .statusBarColor(white)
                .background(HilingualTheme.colors.white)
                .fillMaxSize()
                .subScreenPadding(paddingValues)
                .addFocusCleaner(focusManager),
        ) {
            BackTopAppBar(
                title = "일기 작성하기",
                onBackClicked = {
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = WRITE_DIARY,
                        event = "back_diary",
                        properties = mapOf("back_source" to "ui_button"),
                    )
                    requestExit()
                },
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .advancedImePadding()
                    .verticalScroll(verticalScrollState)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DateText(date = uiState.selectedDate)

                    TextScanButton(
                        onClick = {
                            tracker.logEvent(
                                trigger = TriggerType.CLICK,
                                page = WRITE_DIARY,
                                event = "scan_text",
                            )
                            isImageBottomSheetVisible = true
                        },
                    )
                }

                RecommendedTopicDropdown(
                    topicKo = uiState.topicKo,
                    topicEn = uiState.topicEn,
                    isRecovery = uiState.isRecovery,
                    modifier = Modifier.noRippleClickable {
                        dropdownClickCount++
                        tracker.logEvent(
                            trigger = TriggerType.CLICK,
                            event = "dropdown",
                            properties = mapOf(
                                "recommen_topic" to "${uiState.topicKo}/${uiState.topicEn}",
                                "dropdown_click_count" to dropdownClickCount,
                                "page" to WRITE_DIARY.pageName,
                            ),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                HilingualLongTextField(
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            isTextFieldFocused = focusState.isFocused
                            if (focusState.isFocused) {
                                textFieldFocusedTime = System.currentTimeMillis()
                            } else {
                                if (textFieldFocusedTime != 0L && uiState.diaryText.isNotBlank()) {
                                    tracker.logEvent(
                                        trigger = TriggerType.CLICK,
                                        page = WRITE_DIARY,
                                        event = "textfield",
                                        properties = mapOf(
                                            "text_input_type" to "typed",
                                            "time_to_first_input" to
                                                (System.currentTimeMillis() - textFieldFocusedTime),
                                        ),
                                    )
                                    textFieldFocusedTime = 0L
                                }
                            }
                        },
                    value = uiState.diaryText,
                    onValueChanged = onDiaryTextChange,
                    maxLength = MAX_DIARY_TEXT_LENGTH,
                    onDoneAction = {
                        focusManager.clearFocus()
                    },
                )

                Spacer(modifier = Modifier.height(4.dp))

                PhotoSelectButton(
                    selectedImageUri = uiState.diaryImageUri,
                    onImageSelected = onDiaryImageUriChange,
                )
            }
        }

        val balloonBuilder = rememberBalloonBuilder {
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setBackgroundColor(Color.Transparent)
            setIsVisibleArrow(false)
            setArrowSize(0)
            setDismissWhenTouchOutside(false)
            setIsAttachedInDecor(false)
        }

        val balloonState = rememberBalloonState(balloonBuilder)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            Text(
                text = "피드백을 요청한 일기는 수정이 불가능해요.",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray400,
            )

            HilingualButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .balloon(
                        state = balloonState,
                        balloonContent = {
                            WriteGuideTooltip(
                                text = "10자 이상 작성해야 피드백 요청이 가능해요!",
                            )
                        },
                    ),
                text = "피드백 요청하기",
                enableProvider = { isFeedbackRequestEnabled },
                onClick = onFeedbackRequestClick,
            )
        }

        LaunchedEffect(isFeedbackRequestEnabled, isTextFieldFocused) {
            if (isFeedbackRequestEnabled || isTextFieldFocused) {
                balloonState.dismiss()
                return@LaunchedEffect
            }

            balloonState.showAlignTop()
            delay(WRITE_GUIDE_TOOLTIP_DURATION_MILLIS.milliseconds)
            balloonState.dismiss()
        }
    }
}

private const val WRITE_GUIDE_TOOLTIP_DURATION_MILLIS = 5000L

@Composable
private fun DateText(
    date: LocalDate,
) {
    val formattedDate = remember(date) { date.toKoreanFullDate() }

    Text(
        text = formattedDate,
        style = HilingualTheme.typography.bodyM16,
        color = HilingualTheme.colors.black,
    )
}

@Preview
@Composable
private fun DiaryWriteScreenPreview() {
    var uiState by remember {
        mutableStateOf(
            DiaryWriteUiState(
                topicKo = "오늘 당신을 놀라게 한 일이 있었나요?",
                topicEn = "What surprised you today?",
            ),
        )
    }

    HilingualTheme {
        DiaryWriteScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = uiState,
            onBackClick = {},
            onTempSaveClick = {},
            onDiaryTextChange = { uiState = uiState.copy(diaryText = it) },
            onDiaryImageUriChange = { uiState = uiState.copy(diaryImageUri = it) },
            onCameraClick = {},
            onGalleryClick = {},
            onFeedbackRequestClick = {},
            tracker = FakeTracker(),
        )
    }
}
