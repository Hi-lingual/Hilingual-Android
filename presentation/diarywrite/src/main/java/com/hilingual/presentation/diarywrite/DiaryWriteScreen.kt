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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.advancedImePadding
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualLongTextField
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState
import com.hilingual.presentation.diarywrite.component.DiaryWriteCancelDialog
import com.hilingual.presentation.diarywrite.component.FeedbackCompleteContent
import com.hilingual.presentation.diarywrite.component.FeedbackFailureContent
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
import com.hilingual.presentation.diarywrite.component.FeedbackMedia
import com.hilingual.presentation.diarywrite.component.FeedbackUIData
import com.hilingual.presentation.diarywrite.component.ImageSelectBottomSheet
import com.hilingual.presentation.diarywrite.component.PhotoSelectButton
import com.hilingual.presentation.diarywrite.component.RecommendedTopicDropdown
import com.hilingual.presentation.diarywrite.component.TextScanButton
import com.hilingual.presentation.diarywrite.component.WriteGuideTooltip
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import kotlinx.coroutines.delay
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    val feedbackState by viewModel.feedbackState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current

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

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = white,
            isDarkIcon = true
        )
    }

    when (feedbackState) {
        is DiaryFeedbackState.Default -> {
            DiaryWriteScreen(
                paddingValues = paddingValues,
                onBackClicked = navigateUp,
                selectedDate = uiState.selectedDate,
                topicKo = uiState.topicKo,
                topicEn = uiState.topicEn,
                diaryText = uiState.diaryText,
                onDiaryTextChanged = viewModel::updateDiaryText,
                diaryImageUri = uiState.diaryImageUri,
                onDiaryImageUriChanged = viewModel::updateDiaryImageUri,
                onBottomSheetCameraClicked = {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
                onBottomSheetGalleryClicked = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onDiaryFeedbackRequestButtonClick = viewModel::postDiaryFeedbackCreate
            )
        }

        is DiaryFeedbackState.Loading -> {
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                uiData = FeedbackUIData(
                    title = "일기 저장 중..",
                    description = "피드백을 요청하고 있어요.",
                    media = FeedbackMedia.Lottie(
                        resId = R.raw.lottie_feedback_loading,
                        heightDp = 194.dp
                    )
                ),
                content = { FeedbackLoadingContent() }
            )
        }

        is DiaryFeedbackState.Complete -> {
            val diaryId = (feedbackState as DiaryFeedbackState.Complete).diaryId
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                uiData = FeedbackUIData(
                    title = "AI 피드백 완료!",
                    description = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
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

        is DiaryFeedbackState.Failure -> {
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                uiData = FeedbackUIData(
                    title = "앗! 일시적인 오류가 발생했어요.",
                    media = FeedbackMedia.Image(
                        resId = R.drawable.img_error,
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
    onBackClicked: () -> Unit,
    selectedDate: LocalDate,
    topicKo: String,
    topicEn: String,
    diaryText: String,
    onDiaryTextChanged: (String) -> Unit,
    diaryImageUri: Uri?,
    onDiaryImageUriChanged: (Uri?) -> Unit,
    onBottomSheetCameraClicked: () -> Unit,
    onBottomSheetGalleryClicked: () -> Unit,
    onDiaryFeedbackRequestButtonClick: () -> Unit
) {
    val verticalScrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var isDialogVisible by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    BackHandler {
        cancelDiaryWrite(
            diaryText = diaryText,
            diaryImageUri = diaryImageUri,
            onBackClicked = onBackClicked,
            setDialogVisible = { isDialogVisible = it }
        )
    }

    if (isDialogVisible) {
        DiaryWriteCancelDialog(
            onDismiss = { isDialogVisible = false },
            onNoClick = { isDialogVisible = false },
            onCancelClick = onBackClicked
        )
    }

    if (isBottomSheetVisible) {
        ImageSelectBottomSheet(
            onDismiss = { isBottomSheetVisible = false },
            onCameraSelected = {
                onBottomSheetCameraClicked()
                isBottomSheetVisible = false
            },
            onGallerySelected = {
                onBottomSheetGalleryClicked()
                isBottomSheetVisible = false
            }
        )
    }

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .navigationBarsPadding()
            .addFocusCleaner(focusManager)
    ) {
        BackTopAppBar(
            title = "일기 작성하기",
            onBackClicked = {
                cancelDiaryWrite(
                    diaryText = diaryText,
                    diaryImageUri = diaryImageUri,
                    onBackClicked = onBackClicked,
                    setDialogVisible = { isDialogVisible = it }
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateText(
                selectedDateProvider = { selectedDate }
            )

            TextScanButton(
                onClick = { isBottomSheetVisible = true }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .advancedImePadding()
                .verticalScroll(verticalScrollState)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            RecommendedTopicDropdown(
                enTopic = topicEn,
                koTopic = topicKo,
                focusManager = focusManager
            )

            Spacer(modifier = Modifier.height(8.dp))

            HilingualLongTextField(
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

        Balloon(
            builder = rememberBalloonBuilder {
                setWidth(BalloonSizeSpec.WRAP)
                setHeight(BalloonSizeSpec.WRAP)
                setBackgroundColor(Color.Transparent)
                setIsVisibleArrow(false)
                setArrowSize(0)
                setDismissWhenTouchOutside(false)
                setIsAttachedInDecor(false)
            },
            balloonContent = {
                WriteGuideTooltip(
                    text = "10자 이상 작성해야 피드백 요청이 가능해요!"
                )
            }
        ) { balloon ->
            LaunchedEffect(Unit) {
                balloon.showAlignTop()
                delay(5000)
                balloon.dismiss()
            }
            HilingualButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = "피드백 요청하기",
                enableProvider = { diaryText.length >= 10 },
                onClick = onDiaryFeedbackRequestButtonClick
            )
        }
    }
}

private fun cancelDiaryWrite(
    diaryText: String,
    diaryImageUri: Uri?,
    onBackClicked: () -> Unit,
    setDialogVisible: (Boolean) -> Unit
) {
    if (diaryText.isNotBlank() || diaryImageUri != null) {
        setDialogVisible(true)
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
        selectedDate.format(DATE_FORMATTER)
    }

    Text(
        text = formattedDate,
        style = HilingualTheme.typography.bodySB16,
        color = HilingualTheme.colors.black
    )
}

private val DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)

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
            onBackClicked = {},
            selectedDate = LocalDate.now(),
            topicKo = "오늘 당신을 놀라게 한 일이 있었나요?",
            topicEn = "What surprised you today?",
            diaryText = diaryText,
            onDiaryTextChanged = { diaryText = it },
            diaryImageUri = diaryImageUri,
            onDiaryImageUriChanged = { diaryImageUri = it },
            onBottomSheetCameraClicked = {},
            onBottomSheetGalleryClicked = {},
            onDiaryFeedbackRequestButtonClick = {}
        )
    }
}
