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
package com.hilingual.core.ui.component.item.feed

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.util.formatRelativeTime
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.dropdown.HilingualBasicDropdownMenu
import com.hilingual.core.designsystem.component.dropdown.HilingualDropdownMenuItem
import com.hilingual.core.designsystem.component.image.ErrorImageSize
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.ui.component.dialog.report.ReportPostDialog
import com.hilingual.core.ui.component.image.ProfileImage
import kotlinx.coroutines.launch

@Composable
fun FeedCard(
    profileUrl: String?,
    onProfileClick: () -> Unit,
    nickname: String,
    streak: Int?,
    sharedDateInMinutes: Long,
    content: String,
    onContentDetailClick: () -> Unit,
    imageUrl: String?,
    likeCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isMine: Boolean,
    onUnpublishClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(HilingualTheme.colors.white)
            .padding(vertical = 20.dp)
    ) {
        ProfileImage(
            imageUrl = profileUrl,
            modifier = Modifier
                .size(42.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray200,
                    shape = CircleShape
                )
                .noRippleClickable(onClick = onProfileClick)
        )

        Column {
            FeedHeader(
                nickname = nickname,
                streak = streak,
                sharedDateInMinutes = sharedDateInMinutes,
                isDropdownExpanded = isDropdownExpanded,
                onDropdownExpandedChange = { isDropdownExpanded = it },
                isMine = isMine,
                onUnpublishClick = onUnpublishClick,
                onReportClick = onReportClick
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier.noRippleClickable(onClick = onContentDetailClick)
            ) {
                Text(
                    text = content,
                    style = HilingualTheme.typography.bodyR16,
                    color = HilingualTheme.colors.black,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )

                if (imageUrl != null) {
                    NetworkImage(
                        imageUrl = imageUrl,
                        shape = RoundedCornerShape(8.dp),
                        errorImageSize = ErrorImageSize.LARGE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.6f)
                            .padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FeedFooter(
                likeCount = likeCount,
                isLiked = isLiked,
                onLikeClick = onLikeClick,
                onMoreClick = onContentDetailClick
            )
        }
    }
}

@Composable
private fun FeedHeader(
    nickname: String,
    streak: Int?,
    sharedDateInMinutes: Long,
    isDropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    isMine: Boolean,
    onUnpublishClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(sharedDateInMinutes) { formatRelativeTime(sharedDateInMinutes) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        Text(
            text = nickname,
            style = HilingualTheme.typography.headSB16,
            color = HilingualTheme.colors.gray850
        )

        if (streak != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire_16),
                contentDescription = null,
                tint = HilingualTheme.colors.hilingualOrange,
                modifier = Modifier
                    .padding(start = 4.dp, end = 1.dp)
                    .size(16.dp)
            )

            Text(
                text = "$streak",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.hilingualOrange
            )
        }

        Text(
            text = formattedDate,
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray400,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        FeedDropDownMenu(
            isExpanded = isDropdownExpanded,
            isMine = isMine,
            onExpandedChange = onDropdownExpandedChange,
            onActionClick = {
                if (isMine) {
                    onUnpublishClick()
                } else {
                    onReportClick()
                }
            }
        )
    }
}

@Composable
private fun FeedFooter(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    likeCount: Int,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = if (isLiked) R.drawable.ic_like_24_filled else R.drawable.ic_like_24_empty
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(
                        onClick = {
                            onLikeClick()
                            if (!isLiked) {
                                coroutineScope.launch {
                                    scale.snapTo(0.5f)
                                    scale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        }
                    )
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
            )
            Text(
                text = likeCount.toString(),
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.black
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.noRippleClickable(onClick = onMoreClick)
        ) {
            Text(
                text = "상세보기",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray400
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right_16_thin),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}

@Composable
private fun FeedDropDownMenu(
    isExpanded: Boolean,
    isMine: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onActionClick: () -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    HilingualBasicDropdownMenu(
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        HilingualDropdownMenuItem(
            text = if (isMine) "비공개하기" else "게시글 신고하기",
            iconResId = if (isMine) R.drawable.ic_hide_24 else R.drawable.ic_report_24,
            onClick = {
                isDialogVisible = true
                onExpandedChange(false)
            }
        )
    }

    if (isMine) {
        DiaryUnpublishDialog(
            isVisible = isDialogVisible,
            onDismiss = { isDialogVisible = false },
            onPrivateClick = {
                isDialogVisible = false
                onActionClick()
            }
        )
    } else {
        ReportPostDialog(
            isVisible = isDialogVisible,
            onDismiss = { isDialogVisible = false },
            onReportClick = {
                isDialogVisible = false
                onActionClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedHeaderPreview() {
    HilingualTheme {
        FeedHeader(
            nickname = "HilingualUser",
            streak = 10,
            sharedDateInMinutes = 6000,
            isDropdownExpanded = false,
            onDropdownExpandedChange = {},
            isMine = true,
            onUnpublishClick = {},
            onReportClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedHeaderPreviewNoStreak() {
    HilingualTheme {
        FeedHeader(
            nickname = "HilingualUser",
            streak = null,
            sharedDateInMinutes = 6000,
            isDropdownExpanded = false,
            onDropdownExpandedChange = {},
            isMine = true,
            onUnpublishClick = {},
            onReportClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedFooterPreview() {
    HilingualTheme {
        var isLikedPreview by remember { mutableStateOf(true) }
        var likeCountPreview by remember { mutableStateOf(6) }

        FeedFooter(
            likeCount = likeCountPreview,
            isLiked = isLikedPreview,
            onLikeClick = {
                isLikedPreview = !isLikedPreview
                if (isLikedPreview) {
                    likeCountPreview++
                } else {
                    likeCountPreview--
                }
            },
            onMoreClick = {}
        )
    }
}

@Preview()
@Composable
private fun FeedContentPreviewWithImage() {
    HilingualTheme {
        var isLiked by remember { mutableStateOf(false) }
        var likeCount by remember { mutableStateOf(25) }

        FeedCard(
            profileUrl = "https://picsum.photos/id/237/200/200",
            nickname = "HilingualDev",
            streak = 7,
            sharedDateInMinutes = 3,
            content = "Today was a busy but fulfilling day. I spent the morning working on my project and finally solved a problem that had been bothering me for days. " +
                "In the afternoon, I met a friend for coffee and we talked about our future plans.\n" +
                "The weather was warm and sunny, which made the walk back home really pleasant.\n" +
                "I feel tired now, but also proud of how I spent my day.",
            imageUrl = "https://picsum.photos/id/1060/800/600",
            likeCount = likeCount,
            isLiked = isLiked,
            onProfileClick = { /*프로필 클릭 로직*/ },
            onContentDetailClick = { /* 상세보기 클릭 로직 */ },
            onLikeClick = {
                isLiked = !isLiked
                if (isLiked) likeCount++ else likeCount--
            },
            isMine = true,
            onUnpublishClick = { },
            onReportClick = { }
        )
    }
}

@Preview()
@Composable
private fun FeedContentPreviewNoImage() {
    HilingualTheme {
        var isLiked by remember { mutableStateOf(true) }
        var likeCount by remember { mutableStateOf(102) }

        FeedCard(
            profileUrl = "",
            nickname = "User123",
            streak = null,
            sharedDateInMinutes = 24,
            content = "Today was a busy but fulfilling day.\n" +
                "I spent the morning working on my project and finally solved a problem that had been bothering me for days.\n" +
                "In the afternoon, I met a friend for coffee and we talked about our future plans.\n" +
                "The weather was warm and sunny, which made the walk back home really pleasant.\n" +
                "I feel tired now, but also proud of how I spent my day.",
            imageUrl = null,
            likeCount = likeCount,
            isLiked = isLiked,
            onProfileClick = { /*프로필 클릭 로직*/ },
            onContentDetailClick = { /* 상세보기 클릭 로직 */ },
            onLikeClick = {
                isLiked = !isLiked
                if (isLiked) likeCount++ else likeCount--
            },
            isMine = true,
            onUnpublishClick = { },
            onReportClick = { }
        )
    }
}
