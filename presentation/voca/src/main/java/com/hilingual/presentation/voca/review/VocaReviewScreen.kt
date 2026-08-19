/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.presentation.voca.review

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.RetryOnReconnect
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.component.view.HilingualLoadErrorView
import com.hilingual.core.designsystem.component.view.LoadErrorViewAction
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.component.rememberVocaTts
import com.hilingual.presentation.voca.review.component.ReviewCardSection
import com.hilingual.presentation.voca.review.component.ReviewResultContent
import kotlin.math.min
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun VocaReviewRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateUpWithSaved: () -> Unit,
    viewModel: VocaReviewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current
    val ttsState = rememberVocaTts()

    BackHandler { viewModel.onBackPressed() }

    RetryOnReconnect(
        isLoading = uiState.cards is UiState.Loading,
        shouldRetry = uiState.cards is UiState.Failure,
        onRetry = viewModel::retryLoad,
    )

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            VocaReviewSideEffect.ShowErrorDialog -> dialogTrigger.show(onClick = viewModel::saveResults)
        }
    }

    LaunchedEffect(uiState.exitReason) {
        when (uiState.exitReason) {
            ReviewExitReason.SAVED -> navigateUpWithSaved()
            ReviewExitReason.EMPTY_DECK, ReviewExitReason.CANCELLED -> navigateUp()
            null -> {}
        }
    }

    LaunchedEffect(uiState.currentIndex, uiState.phase) {
        ttsState.stop()
    }

    when (val cardsState = uiState.cards) {
        is UiState.Loading -> {
            HilingualLoadingIndicator(
                backgroundColor = HilingualTheme.colors.gray100,
                modifier = Modifier
                    .statusBarColor(HilingualTheme.colors.gray100)
                    .padding(paddingValues),
            )
        }

        is UiState.Failure -> {
            HilingualLoadErrorView(
                action = LoadErrorViewAction.Retry(
                    onRetryClick = viewModel::retryLoad,
                ),
                modifier = Modifier.padding(paddingValues),
            )
        }

        is UiState.Success -> {
            when (uiState.phase) {
                ReviewPhase.REVIEWING, ReviewPhase.EXIT_CONFIRM -> {
                    Box {
                        ReviewContent(
                            paddingValues = paddingValues,
                            cards = cardsState.data,
                            currentIndex = uiState.currentIndex,
                            onBackClick = viewModel::onBackPressed,
                            onJudge = viewModel::judgeCurrentCard,
                            onCardDismissed = viewModel::moveToNextCard,
                            onTtsClick = { ttsState.toggle(it) },
                        )

                        if (uiState.phase == ReviewPhase.EXIT_CONFIRM) {
                            ReviewResultContent(
                                paddingValues = paddingValues,
                                imageRes = DesignSystemR.drawable.img_brain_pencil,
                                imageModifier = Modifier.height(150.dp),
                                message = "잠시만요!\n복습한 단어를 저장할까요?",
                                buttonText = "저장하기",
                                onButtonClick = viewModel::saveResults,
                                isSaving = uiState.isSaving,
                                onCloseClick = viewModel::exitWithoutSaving,
                                modifier = Modifier.noRippleClickable {},
                            )
                        }
                    }
                }

                ReviewPhase.COMPLETED -> {
                    ReviewResultContent(
                        paddingValues = paddingValues,
                        imageRes = DesignSystemR.drawable.img_review_finish,
                        imageModifier = Modifier.height(180.dp),
                        message = "단어를 모두 복습했어요.\n노력하는 당신이 대단해요!",
                        buttonText = "완료",
                        onButtonClick = viewModel::saveResults,
                        isSaving = uiState.isSaving,
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun ReviewContent(
    paddingValues: PaddingValues,
    cards: ImmutableList<ReviewCardUiModel>,
    currentIndex: Int,
    onBackClick: () -> Unit,
    onJudge: (Boolean) -> Unit,
    onCardDismissed: () -> Unit,
    onTtsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isHintVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.gray100)
            .background(HilingualTheme.colors.gray100)
            .padding(paddingValues),
    ) {
        HilingualBasicTopAppBar(
            title = "${cards.size - currentIndex}개",
            backgroundColor = HilingualTheme.colors.gray100,
            navigationIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_arrow_left_24_back),
                    contentDescription = null,
                    tint = HilingualTheme.colors.black,
                    modifier = Modifier.noRippleClickable(onClick = onBackClick),
                )
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "가볍게 탭하여 한글 뜻을 확인하세요.",
            style = HilingualTheme.typography.bodyR14,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (isHintVisible) 1f else 0f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReviewCardSection(
            card = cards[currentIndex],
            behindCardCount = min(cards.lastIndex - currentIndex, 2),
            onFlip = { isHintVisible = false },
            onJudge = onJudge,
            onCardDismissed = onCardDismissed,
            onTtsClick = onTtsClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewContentPreview() {
    HilingualTheme {
        ReviewContent(
            paddingValues = PaddingValues(0.dp),
            cards = persistentListOf(
                ReviewCardUiModel(
                    phraseId = 1L,
                    phrase = "food for thought",
                    phraseType = persistentListOf("형용사", "숙어"),
                    explanation = "생각할 거리",
                ),
            ),
            currentIndex = 0,
            onBackClick = {},
            onJudge = {},
            onCardDismissed = {},
            onTtsClick = {},
        )
    }
}
