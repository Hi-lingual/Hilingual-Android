package com.hilingual.presentation.feeddiary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feeddiary.component.DiaryBlockBottomSheet
import com.hilingual.presentation.feeddiary.component.FeedDiaryProfile
import com.hilingual.presentation.feeddiary.model.ProfileContentUiModel

@Composable
internal fun FeedDiaryRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: FeedDiaryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var isImageDetailVisible by remember { mutableStateOf(false) }

    BackHandler {
        if (isImageDetailVisible) {
            isImageDetailVisible = false
        } else {
            navigateUp()
        }
    }

    when (state) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            FeedDiaryScreen(
                paddingValues = paddingValues,
                uiState = (state as UiState.Success<FeedDiaryUiState>).data,
                onBackClick = navigateUp,
                onProfileClick = {},
                onLikeClick = {},
            )
        }

        else -> {}
    }
}

@Composable
private fun FeedDiaryScreen(
    paddingValues: PaddingValues,
    uiState: FeedDiaryUiState,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isUnpublishBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportBottomSheetVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackAndMoreTopAppBar(
            onBackClicked = onBackClick,
            onMoreClicked = {},
            title = null
        )

        with(uiState.profileContent) {
            FeedDiaryProfile(
                profileUrl = profileUrl,
                nickname = nickname,
                streak = streak,
                isLiked = isLiked,
                likeCount = likeCount,
                sharedDateInMinutes = sharedDateInMinutes,
                onProfileClick = onProfileClick,
                onLikeClick = onLikeClick
            )
        }
    }

    if (uiState.isMine) {
        DiaryUnpublishDialog(
            isVisible = isUnpublishBottomSheetVisible,
            onDismiss = { isUnpublishBottomSheetVisible = false },
            onPrivateClick = {
                // TODO: 일기 비공개 API 호출
                isUnpublishBottomSheetVisible = false
            }
        )
    } else {
        DiaryBlockBottomSheet(
            isVisible = isReportBottomSheetVisible,
            onDismiss = { isReportBottomSheetVisible = false },
            onReportClick = {
                isReportBottomSheetVisible = false
                //TODO: 신고폼으로 이동
            },
            onBlockClick = {
                isReportBottomSheetVisible = false
                //TODO: 계정 차단 확인 모달 표시
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedDiaryScreenPreview() {
    HilingualTheme {
        FeedDiaryScreen(
            paddingValues = PaddingValues(),
            onBackClick = {},
            onProfileClick = {},
            onLikeClick = {},
            uiState = FeedDiaryUiState(
                writtenDate = "8월 23일 토요일",
                profileContent = ProfileContentUiModel(
                    profileUrl = "",
                    nickname = "작나",
                    streak = 2,
                    isLiked = true,
                    likeCount = 112,
                    sharedDateInMinutes = 3
                )
            )
        )
    }
}
