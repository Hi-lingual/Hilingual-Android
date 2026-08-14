package com.hilingual.presentation.voca.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.dropShadow
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.item.voca.WordPhraseTypeTag
import com.hilingual.presentation.voca.review.ReviewCardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hilingual.core.designsystem.R as DesignSystemR

private const val TINT_MAX_ALPHA = 0.3f

private val DontKnowTint = Color(0xFFF27A3C)

@Composable
internal fun FlashCard(
    card: ReviewCardUiModel,
    flipRotation: () -> Float,
    isRightSwipe: Boolean,
    contentAlpha: () -> Float,
    tintProgress: () -> Float,
    stampAlpha: () -> Float,
    onTtsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(20.dp)
    val tintColor = if (isRightSwipe) HilingualTheme.colors.hilingualBlue else DontKnowTint
    val isFrontVisible by remember { derivedStateOf { flipRotation() <= 90f } }

    Box(
        modifier = modifier
            .dropShadow(
                shape = cardShape,
                color = HilingualTheme.colors.black,
                alpha = 0.08f,
                offsetX = 0.dp,
                offsetY = 2.dp,
                blur = 8.dp,
                spread = 0.dp,
            )
            .clip(cardShape)
            .background(HilingualTheme.colors.white),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    rotationY = flipRotation()
                    cameraDistance = 8 * density
                    alpha = contentAlpha()
                },
        ) {
            if (isFrontVisible) {
                FlashCardFace(
                    text = card.phrase,
                    phraseType = card.phraseType,
                    isTtsVisible = true,
                    onTtsClick = onTtsClick,
                )
            } else {
                FlashCardFace(
                    text = card.explanation,
                    phraseType = card.phraseType,
                    isTtsVisible = false,
                    onTtsClick = {},
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                )
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRect(color = tintColor.copy(alpha = TINT_MAX_ALPHA * tintProgress()))
                },
        )

        SwipeStamp(
            isRightSwipe = isRightSwipe,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp)
                .graphicsLayer { alpha = stampAlpha() },
        )
    }
}

@Composable
private fun FlashCardFace(
    text: String,
    phraseType: ImmutableList<String>,
    isTtsVisible: Boolean,
    onTtsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            phraseType.forEach { type ->
                key(type) {
                    WordPhraseTypeTag(phraseType = type)
                }
            }
        }

        if (isTtsVisible) {
            Icon(
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_play_24_and),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .noRippleClickable(onClick = onTtsClick),
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = text,
                style = HilingualTheme.typography.headSB20,
                color = HilingualTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Icon(
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_reverse_28),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun SwipeStamp(
    isRightSwipe: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = HilingualTheme.colors.white, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = if (isRightSwipe) {
                        DesignSystemR.drawable.ic_stamp_check_24
                    } else {
                        DesignSystemR.drawable.ic_stamp_question_24
                    },
                ),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
        Text(
            text = if (isRightSwipe) "알아요" else "몰라요",
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashCardPreview() {
    HilingualTheme {
        FlashCard(
            card = ReviewCardUiModel(
                phraseId = 1L,
                phrase = "food for thought",
                phraseType = persistentListOf("형용사", "숙어"),
                explanation = "생각할 거리",
            ),
            flipRotation = { 0f },
            isRightSwipe = true,
            contentAlpha = { 1f },
            tintProgress = { 0f },
            stampAlpha = { 0f },
            onTtsClick = {},
            modifier = Modifier.size(width = 328.dp, height = 400.dp),
        )
    }
}
