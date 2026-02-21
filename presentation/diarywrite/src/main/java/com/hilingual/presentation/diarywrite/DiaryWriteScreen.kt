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

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page.WRITE_DIARY
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.advancedImePadding
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
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
import com.hilingual.presentation.diarywrite.component.FeedbackCompleteContent
import com.hilingual.presentation.diarywrite.component.FeedbackFailureContent
import com.hilingual.presentation.diarywrite.component.FeedbackMedia
import com.hilingual.presentation.diarywrite.component.FeedbackUIData
import com.hilingual.presentation.diarywrite.component.ImageSelectBottomSheet
import com.hilingual.presentation.diarywrite.component.PhotoSelectButton
import com.hilingual.presentation.diarywrite.component.RecommendedTopicDropdown
import com.hilingual.presentation.diarywrite.component.TextScanButton
import com.hilingual.presentation.diarywrite.component.WriteGuideTooltip
import com.hilingual.presentation.diarywrite.screen.DiaryFeedbackLoadingScreen
import com.hilingual.presentation.diarywrite.screen.DiaryFeedbackStatusScreen
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.rememberBalloonState
import com.skydoves.balloon.compose.setBackgroundColor
import java.io.File
import java.time.LocalDate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun DiaryWriteRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    viewModel: DiaryWriteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current
    val messageController = LocalMessageController.current
    val feedbackUiState by viewModel.feedbackUiState.collectAsStateWithLifecycle()
    val tracker = LocalTracker.current

    val lottieComposition1 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_feedback_loading_1)
    )
    val lottieComposition2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_feedback_loading_2)
    )
    val lottieComposition3 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_feedback_loading_3)
    )

    val lottieCompositions = remember(lottieComposition1, lottieComposition2, lottieComposition3) {
        persistentListOf(lottieComposition1, lottieComposition2, lottieComposition3)
    }

    var diaryTextImageUri by remember { mutableStateOf<Uri?>(null) }

    var diaryTextImageFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val uri = diaryTextImageUri
            if (uri != null) {
                viewModel.extractTextFromImage(uri, diaryTextImageFile)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val (uri, file) = createTempImageFile(context)
            diaryTextImageUri = uri
            diaryTextImageFile = file
            cameraLauncher.launch(uri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.extractTextFromImage(it) }
    }

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
                isDiaryTempExist = uiState.isDiaryTempExist,
                onBackClicked = navigateUp,
                onTempSaveClick = viewModel::handleDiaryTempSavingFlow,
                selectedDate = uiState.selectedDate,
                topicKo = uiState.topicKo,
                topicEn = uiState.topicEn,
                diaryText = uiState.diaryText,
                initialDiaryText = uiState.initialDiaryText,
                onDiaryTextChanged = viewModel::updateDiaryText,
                diaryImageUri = uiState.diaryImageUri,
                initialDiaryImageUri = uiState.initialDiaryImageUri,
                onDiaryImageUriChanged = viewModel::updateDiaryImageUri,
                onBottomSheetCameraClicked = {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
                onBottomSheetGalleryClicked = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onDiaryFeedbackRequestButtonClick = {
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = WRITE_DIARY,
                        event = "submit_cta",
                        properties = mapOf(
                            "has_photo" to (uiState.diaryImageUri != null),
                            "char_count" to uiState.diaryText.length
                        )
                    )
                    viewModel.postDiaryFeedbackCreate()
                },
                tracker = tracker
            )
        }

        is UiState.Loading -> {
            DiaryFeedbackLoadingScreen(
                lottieCompositions = lottieCompositions,
                paddingValues = paddingValues
            )
        }

        is UiState.Success -> {
            val diaryId = feedbackState.data
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                uiData = FeedbackUIData(
                    title = "일기 저장 완료!",
                    description = {
                        Text(
                            text = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
                            color = HilingualTheme.colors.gray400,
                            style = HilingualTheme.typography.headR18,
                            textAlign = TextAlign.Center
                        )
                    },
                    media = FeedbackMedia.Lottie(
                        resId = R.raw.lottie_feedback_complete,
                        heightDp = 180.dp
                    )
                ),
                content = {
                    FeedbackCompleteContent(
                        diaryId = diaryId,
                        onCloseButtonClick = navigateToHome,
                        onShowFeedbackButtonClick = navigateToDiaryFeedback
                    )
                }
            )
        }

        is UiState.Failure -> {
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                uiData = FeedbackUIData(
                    title = "앗! 일시적인 오류가 발생했어요.",
                    media = FeedbackMedia.Image(
                        resId = DesignSystemR.drawable.img_error,
                        heightDp = 175.dp
                    )
                ),
                content = {
                    FeedbackFailureContent(
                        onCloseButtonClick = navigateToHome,
                        onRequestAgainButtonClick = viewModel::postDiaryFeedbackCreate
                    )
                }
            )
        }
    }
}

@Composable
private fun DiaryWriteScreen(
    paddingValues: PaddingValues,
    isDiaryTempExist: Boolean,
    onBackClicked: () -> Unit,
    onTempSaveClick: () -> Unit,
    selectedDate: LocalDate,
    topicKo: String,
    topicEn: String,
    diaryText: String,
    initialDiaryText: String,
    onDiaryTextChanged: (String) -> Unit,
    diaryImageUri: Uri?,
    initialDiaryImageUri: Uri?,
    onDiaryImageUriChanged: (Uri?) -> Unit,
    onBottomSheetCameraClicked: () -> Unit,
    onBottomSheetGalleryClicked: () -> Unit,
    onDiaryFeedbackRequestButtonClick: () -> Unit,
    tracker: Tracker
) {
    val verticalScrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    var isCancelBottomSheetVisible by remember { mutableStateOf(false) }
    var isOverwriteDialogVisible by remember { mutableStateOf(false) }
    var isImageBottomSheetVisible by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    var dropdownClickCount by remember { mutableIntStateOf(0) }
    var textFieldFocusedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        cancelDiaryWrite(
            initialText = initialDiaryText,
            currentText = diaryText,
            initialImageUri = initialDiaryImageUri,
            currentImageUri = diaryImageUri,
            onBackClicked = onBackClicked,
            setBottomSheetVisible = { isCancelBottomSheetVisible = it }
        )
    }

    DiaryWriteCancelBottomSheet(
        isVisible = isCancelBottomSheetVisible,
        onDismiss = { isCancelBottomSheetVisible = false },
        onCancelClick = {
            tracker.logEvent(
                trigger = TriggerType.CLICK,
                page = WRITE_DIARY,
                event = "modal",
                properties = mapOf("modal_action" to "confirm_exit")
            )
            onBackClicked()
        },
        onTempSaveClick = {
            if (isDiaryTempExist) {
                isCancelBottomSheetVisible = false
                isOverwriteDialogVisible = true
            } else {
                onTempSaveClick()
            }
        }
    )

    DiaryOverwriteDialog(
        isVisible = isOverwriteDialogVisible,
        onDismiss = { isOverwriteDialogVisible = false },
        onNoClick = { isOverwriteDialogVisible = false },
        onOverwriteClick = onTempSaveClick
    )

    ImageSelectBottomSheet(
        isVisible = isImageBottomSheetVisible,
        onDismiss = { isImageBottomSheetVisible = false },
        onCameraSelected = {
            onBottomSheetCameraClicked()
            isImageBottomSheetVisible = false
        },
        onGallerySelected = {
            onBottomSheetGalleryClicked()
            isImageBottomSheetVisible = false
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .statusBarColor(white)
                .background(HilingualTheme.colors.white)
                .fillMaxSize()
                .padding(paddingValues)
                .addFocusCleaner(focusManager)
        ) {
            BackTopAppBar(
                title = "일기 작성하기",
                onBackClicked = {
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = WRITE_DIARY,
                        event = "back_diary",
                        properties = mapOf("back_source" to "ui_button")
                    )
                    cancelDiaryWrite(
                        initialText = initialDiaryText,
                        currentText = diaryText,
                        initialImageUri = initialDiaryImageUri,
                        currentImageUri = diaryImageUri,
                        onBackClicked = onBackClicked,
                        setBottomSheetVisible = { isCancelBottomSheetVisible = it }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .advancedImePadding()
                    .verticalScroll(verticalScrollState)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DateText(
                        selectedDateProvider = { selectedDate }
                    )

                    TextScanButton(
                        onClick = {
                            tracker.logEvent(
                                trigger = TriggerType.CLICK,
                                page = WRITE_DIARY,
                                event = "scan_text"
                            )
                            isImageBottomSheetVisible = true
                        }
                    )
                }

                RecommendedTopicDropdown(
                    enTopic = topicEn,
                    koTopic = topicKo,
                    focusManager = focusManager,
                    modifier = Modifier.noRippleClickable {
                        dropdownClickCount++
                        tracker.logEvent(
                            trigger = TriggerType.CLICK,
                            page = WRITE_DIARY,
                            event = "dropdown",
                            properties = mapOf(
                                "recommen_topic" to "$topicKo/$topicEn",
                                "dropdown_click_count" to dropdownClickCount
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                HilingualLongTextField(
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            isTextFieldFocused = focusState.isFocused
                            if (focusState.isFocused) {
                                textFieldFocusedTime = System.currentTimeMillis()
                            } else {
                                if (textFieldFocusedTime != 0L && diaryText.isNotBlank()) {
                                    tracker.logEvent(
                                        trigger = TriggerType.CLICK,
                                        page = WRITE_DIARY,
                                        event = "textfield",
                                        properties = mapOf(
                                            "text_input_type" to "typed",
                                            "time_to_first_input" to (System.currentTimeMillis() - textFieldFocusedTime)
                                        )
                                    )
                                    textFieldFocusedTime = 0L
                                }
                            }
                        },
                    value = diaryText,
                    onValueChanged = onDiaryTextChanged,
                    maxLength = 1000,
                    onDoneAction = {
                        focusManager.clearFocus()
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                PhotoSelectButton(
                    selectedImgUri = diaryImageUri,
                    onImgSelected = onDiaryImageUriChanged
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
                .navigationBarsPadding()
        ) {
            Text(
                text = "피드백을 요청한 일기는 수정이 불가능해요.",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray400
            )

            HilingualButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .balloon(
                        state = balloonState,
                        balloonContent = {
                            WriteGuideTooltip(
                                text = "10자 이상 작성해야 피드백 요청이 가능해요!"
                            )
                        }
                    ),
                text = "피드백 요청하기",
                enableProvider = { diaryText.length >= 10 },
                onClick = onDiaryFeedbackRequestButtonClick
            )
        }

        LaunchedEffect(Unit) {
            balloonState.showAlignTop()
            delay(5000)
            balloonState.dismiss()
        }

        LaunchedEffect(isTextFieldFocused) {
            if (isTextFieldFocused) balloonState.dismiss()
        }
    }
}

private fun cancelDiaryWrite(
    initialText: String,
    currentText: String,
    initialImageUri: Uri?,
    currentImageUri: Uri?,
    onBackClicked: () -> Unit,
    setBottomSheetVisible: (Boolean) -> Unit
) {
    val isChanged = initialText != currentText || initialImageUri != currentImageUri
    if (isChanged) {
        setBottomSheetVisible(true)
    } else {
        onBackClicked()
    }
}

@Composable
private fun DateText(
    selectedDateProvider: () -> LocalDate
) {
    val selectedDate = selectedDateProvider()

    val formattedDate = remember(selectedDate) {
        selectedDate.toKoreanFullDate()
    }

    Text(
        text = formattedDate,
        style = HilingualTheme.typography.bodyM16,
        color = HilingualTheme.colors.black
    )
}

private fun createTempImageFile(context: Context): Pair<Uri, File> {
    val imageFile = File.createTempFile("camera_", ".jpg", context.cacheDir)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
    return uri to imageFile
}

@Preview
@Composable
private fun DiaryWriteScreenPreview() {
    var diaryText by remember { mutableStateOf("") }
    var diaryImageUri by remember { mutableStateOf<Uri?>(null) }

    HilingualTheme {
        DiaryWriteScreen(
            paddingValues = PaddingValues(0.dp),
            isDiaryTempExist = false,
            onBackClicked = {},
            onTempSaveClick = {},
            selectedDate = LocalDate.now(),
            topicKo = "오늘 당신을 놀라게 한 일이 있었나요?",
            topicEn = "What surprised you today?",
            diaryText = diaryText,
            initialDiaryText = "",
            onDiaryTextChanged = { diaryText = it },
            diaryImageUri = diaryImageUri,
            initialDiaryImageUri = null,
            onDiaryImageUriChanged = { diaryImageUri = it },
            onBottomSheetCameraClicked = {},
            onBottomSheetGalleryClicked = {},
            onDiaryFeedbackRequestButtonClick = {},
            tracker = FakeTracker()
        )
    }
}
