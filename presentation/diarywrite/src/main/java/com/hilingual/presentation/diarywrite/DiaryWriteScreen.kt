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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualLongTextField
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.diarywrite.component.DateText
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

@Composable
internal fun DiaryWriteRoute(
    paddingValues: PaddingValues
) {
    val systemUiController = rememberSystemUiController()
    var diaryText by remember { mutableStateOf("") }
    val diaryImageUri by remember { mutableStateOf<Uri?>(null) }

    SideEffect {
        systemUiController.setStatusBarColor(color = white, darkIcons = false)
    }

    DiaryWriteScreen(
        paddingValues = paddingValues,
        onBackClicked = {},
        selectedDate = LocalDate.now(),
        topicKo = "",
        topicEn = "",
        diaryText = diaryText,
        onDiaryTextChanged = { diaryText = it },
        diaryImageUri = diaryImageUri,
        onDiaryFeedbackRequestButtonClick = {}
    )
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
    onDiaryFeedbackRequestButtonClick: () -> Unit
) {
    val verticalScrollState = rememberScrollState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }

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
    ) {
        BackTopAppBar(
            title = "일기 작성하기",
            onBackClicked = onBackClicked
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp),
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
                maxLength = 1000
            )

            Spacer(modifier = Modifier.height(12.dp))

            PhotoSelectButton(
                onPhotoSelectClick = { /* TODO: 갤러리로 이동 */ },
                onDeleteClick = { /* TODO: 컴포넌트 내에서 해당 작업 처리되도록 리팩토링 예정 */ },
                selectedImgUri = diaryImageUri
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
            // TODO: 10자 이상일 때만 버튼 활성화되도록 처리
            HilingualButton(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = "피드백 요청",
                enabled = diaryText.length >= 10,
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

@Preview
@Composable
private fun DiaryWriteScreenPreview() {
    var diaryText by remember { mutableStateOf("") }

    HilingualTheme {
        DiaryWriteScreen(
            paddingValues = PaddingValues(0.dp),
            onBackClicked = {},
            selectedDate = LocalDate.now(),
            topicKo = "오늘 당신을 놀라게 한 일이 있었나요?",
            topicEn = "What surprised you today?",
            diaryText = diaryText,
            onDiaryTextChanged = { diaryText = it },
            diaryImageUri = null,
            onDiaryFeedbackRequestButtonClick = {}
        )
    }
}