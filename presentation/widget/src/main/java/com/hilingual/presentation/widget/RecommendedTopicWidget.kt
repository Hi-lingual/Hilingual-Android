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
package com.hilingual.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.hilingual.core.designsystem.theme.black
import com.hilingual.core.designsystem.theme.gray100
import com.hilingual.core.designsystem.theme.gray200
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.gray850
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.designsystem.R as DesignSystemR

private val CompactTopicSize = DpSize(110.dp, 110.dp)
private val WideTopicSize = DpSize(250.dp, 110.dp)

class RecommendedTopicWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RecommendedTopicWidget()
}

class RecommendedTopicWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(CompactTopicSize, WideTopicSize),
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            RecommendedTopicWidgetContent(
                state = RecommendedTopicUiState(
                    dateLabel = "12월 17일 월",
                    topic = "What surprised you today?",
                    writingStatus = WritingStatus.UNWRITTEN,
                    remainingHours = 25,
                ),
            )
        }
    }
}

internal data class RecommendedTopicUiState(
    val dateLabel: String,
    val topic: String?,
    val writingStatus: WritingStatus,
    val remainingHours: Int?,
)

internal enum class WritingStatus {
    UNWRITTEN,
    WRITTEN,
}

@Composable
internal fun RecommendedTopicWidgetContent(state: RecommendedTopicUiState) {
    val isWide = LocalSize.current.width >= WideTopicSize.width

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(WidgetColors.surface)
            .cornerRadius(20.dp),
    ) {
        if (isWide) {
            WideTopicHeader(state)
            WideTopicBody(state)
        } else {
            CompactTopicHeader(state.dateLabel)
            CompactTopicBody(state)
        }
    }
}

@Composable
private fun CompactTopicHeader(dateLabel: String) {
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(40.dp)
            .background(WidgetColors.header)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = dateLabel,
            style = TextStyle(
                color = WidgetColors.onHeader,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun CompactTopicBody(state: RecommendedTopicUiState) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = state.topic ?: "지금은 주제를\n불러올 수 없어요",
            modifier = GlanceModifier.defaultWeight(),
            style = TextStyle(
                color = WidgetColors.primaryText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 3,
        )
        if (state.writingStatus == WritingStatus.UNWRITTEN && state.remainingHours != null) {
            RemainingTime(state.remainingHours, compact = true)
        }
    }
}

@Composable
private fun WideTopicHeader(state: RecommendedTopicUiState) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WidgetColors.header)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = state.dateLabel,
            style = TextStyle(
                color = WidgetColors.onHeader,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
        Spacer(modifier = GlanceModifier.width(5.dp))
        Text(
            text = "·",
            style = TextStyle(color = WidgetColors.onHeaderMuted, fontSize = 13.sp),
        )
        Spacer(modifier = GlanceModifier.width(5.dp))
        Text(
            text = if (state.writingStatus == WritingStatus.WRITTEN) "작성완료" else "미작성",
            style = TextStyle(
                color = if (state.writingStatus == WritingStatus.WRITTEN) {
                    WidgetColors.accent
                } else {
                    WidgetColors.onHeaderMuted
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        if (state.writingStatus == WritingStatus.UNWRITTEN && state.remainingHours != null) {
            RemainingTime(state.remainingHours, compact = false)
        }
    }
}

@Composable
private fun WideTopicBody(state: RecommendedTopicUiState) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "오늘의 추천 주제",
            style = TextStyle(color = WidgetColors.secondaryText, fontSize = 11.sp),
        )
        Spacer(modifier = GlanceModifier.height(5.dp))
        Text(
            text = state.topic ?: "지금은 주제를 불러올 수 없어요",
            style = TextStyle(
                color = WidgetColors.primaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 2,
        )
    }
}

@Composable
private fun RemainingTime(
    hours: Int,
    compact: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(DesignSystemR.drawable.ic_time_16),
            contentDescription = null,
            modifier = GlanceModifier.size(if (compact) 14.dp else 16.dp),
        )
        Spacer(modifier = GlanceModifier.width(2.dp))
        Text(
            text = "${hours}시간 남음",
            style = TextStyle(
                color = if (compact) WidgetColors.secondaryText else WidgetColors.onHeaderSecondary,
                fontSize = if (compact) 10.sp else 11.sp,
            ),
        )
    }
}

private object WidgetColors {
    val surface = ColorProvider(day = gray100, night = gray850)
    val header = ColorProvider(day = gray850, night = black)
    val onHeader = ColorProvider(day = white, night = white)
    val onHeaderMuted = ColorProvider(day = gray400, night = gray400)
    val onHeaderSecondary = ColorProvider(day = gray200, night = gray200)
    val primaryText = ColorProvider(day = black, night = white)
    val secondaryText = ColorProvider(day = gray500, night = gray400)
    val accent = ColorProvider(day = hilingualOrange, night = hilingualOrange)
}
