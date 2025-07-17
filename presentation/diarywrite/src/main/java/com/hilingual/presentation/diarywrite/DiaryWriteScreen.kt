package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val feedbackState by viewModel.feedbackState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = white,
            isDarkIcon = false
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
                onDiaryFeedbackRequestButtonClick = { viewModel.postDiaryFeedbackCreate() }
            )
        }

        is DiaryFeedbackState.Loading -> {
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                state = FeedbackUIData(
                    title = "일기 저장 중..",
                    description = "피드백을 요청하고 있어요.",
                    lottieRawRes = R.raw.lottie_feedback_loading,
                    lottieRawResHeightDp = 194.dp
                ),
                content = { FeedbackLoadingContent() }
            )
        }

        is DiaryFeedbackState.Complete -> {
            val diaryId = (feedbackState as DiaryFeedbackState.Complete).diaryId
            DiaryFeedbackStatusScreen(
                paddingValues = paddingValues,
                state = FeedbackUIData(
                    title = "AI 피드백 완료!",
                    description = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
                    lottieRawRes = R.raw.lottie_feedback_complete,
                    lottieRawResHeightDp = 180.dp
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

        is DiaryFeedbackState.Failure -> { /* TODO: 에러 페이지 노출 */
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
    onDiaryFeedbackRequestButtonClick: () -> Unit
) {
    val verticalScrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var isDialogVisible by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

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
            onCameraSelected = { /* TODO: 카메라로 사진 찍기 클릭 시 처리 */ },
            onGallerySelected = { /* TODO: 갤러리에서 선택하기 클릭 시 처리 */ }
        )
    }

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(paddingValues)
            .addFocusCleaner(focusManager)
    ) {
        BackTopAppBar(
            title = "일기 작성하기",
            onBackClicked = { isDialogVisible = true }
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
                koTopic = topicKo
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

            Spacer(modifier = Modifier.height(12.dp))

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
            },
            balloonContent = {
                WriteGuideTooltip(
                    text = "10자 이상 작성해야 피드백 요청이 가능해요!"
                )
            }
        ) { balloon ->
            HilingualButton(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = "피드백 요청하기",
                enableProvider = { diaryText.length >= 10 },
                onClick = onDiaryFeedbackRequestButtonClick
            )

            LaunchedEffect(Unit) {
                balloon.showAlignTop()
                delay(5000)
                balloon.dismiss()
            }
        }
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
            onDiaryFeedbackRequestButtonClick = {}
        )
    }
}
