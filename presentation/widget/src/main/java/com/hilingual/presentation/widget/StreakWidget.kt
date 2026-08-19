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
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
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
import com.hilingual.core.designsystem.theme.gray300
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.gray850
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.designsystem.R as DesignSystemR

private val CompactStreakSize = DpSize(110.dp, 110.dp)
private val WideStreakSize = DpSize(250.dp, 110.dp)

class StreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidget()
}

class StreakWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(CompactStreakSize, WideStreakSize),
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            StreakWidgetContent(
                state = StreakUiState(
                    isLoggedIn = true,
                    streakDays = 3,
                    recentDays = listOf(
                        StreakDay("월", false),
                        StreakDay("화", true),
                        StreakDay("수", true),
                        StreakDay("목", true),
                        StreakDay("금", true),
                    ),
                ),
            )
        }
    }
}

internal data class StreakUiState(
    val isLoggedIn: Boolean,
    val streakDays: Int,
    val recentDays: List<StreakDay>,
)

internal data class StreakDay(
    val label: String,
    val isWritten: Boolean,
)

@Composable
internal fun StreakWidgetContent(state: StreakUiState) {
    val isWide = LocalSize.current.width >= WideStreakSize.width

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(StreakColors.surface)
            .cornerRadius(20.dp)
            .padding(if (isWide) 16.dp else 12.dp),
    ) {
        if (isWide) {
            WideStreakContent(state)
        } else {
            CompactStreakContent(state)
        }
    }
}

@Composable
private fun CompactStreakContent(state: StreakUiState) {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        Text(
            text = "연속 작성",
            style = TextStyle(color = StreakColors.secondaryText, fontSize = 11.sp),
        )
        if (state.isLoggedIn) {
            StreakValue(state.streakDays, compact = true)
        } else {
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = "로그인 후 확인 가능",
                style = TextStyle(
                    color = StreakColors.primaryText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(
                if (state.isLoggedIn && state.streakDays == 0) {
                    DesignSystemR.drawable.img_diary_empty
                } else {
                    DesignSystemR.drawable.img_diary_lock
                },
            ),
            contentDescription = null,
            modifier = GlanceModifier.fillMaxWidth().height(58.dp),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun WideStreakContent(state: StreakUiState) {
    Row(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxHeight()
                .width(118.dp),
        ) {
            Text(
                text = "연속 작성",
                style = TextStyle(color = StreakColors.secondaryText, fontSize = 12.sp),
            )
            if (state.isLoggedIn) {
                StreakValue(state.streakDays, compact = false)
            } else {
                Spacer(modifier = GlanceModifier.height(5.dp))
                Box(
                    modifier = GlanceModifier
                        .width(72.dp)
                        .height(18.dp)
                        .background(StreakColors.lockedBar)
                        .cornerRadius(3.dp),
                ) {}
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = "로그인 후 확인 가능",
                    style = TextStyle(
                        color = StreakColors.primaryText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            Spacer(modifier = GlanceModifier.defaultWeight())
            RecentDays(
                days = state.recentDays,
                enabled = state.isLoggedIn,
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(
                if (state.isLoggedIn && state.streakDays == 0) {
                    DesignSystemR.drawable.img_diary_empty
                } else {
                    DesignSystemR.drawable.img_diary_lock
                },
            ),
            contentDescription = null,
            modifier = GlanceModifier.width(98.dp).height(84.dp),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun StreakValue(
    days: Int,
    compact: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(DesignSystemR.drawable.ic_fire_16),
            contentDescription = null,
            modifier = GlanceModifier.size(if (compact) 22.dp else 30.dp),
        )
        Text(
            text = days.toString(),
            style = TextStyle(
                color = StreakColors.primaryText,
                fontSize = if (compact) 26.sp else 32.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun RecentDays(
    days: List<StreakDay>,
    enabled: Boolean,
) {
    Row(modifier = GlanceModifier.fillMaxWidth()) {
        days.take(5).forEachIndexed { index, day ->
            Box(
                modifier = GlanceModifier
                    .size(19.dp)
                    .background(
                        if (enabled && day.isWritten) {
                            StreakColors.activeDay
                        } else {
                            StreakColors.inactiveDay
                        },
                    ).cornerRadius(10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (enabled) day.label else "",
                    style = TextStyle(
                        color = if (day.isWritten) StreakColors.onActiveDay else StreakColors.secondaryText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            if (index < minOf(days.lastIndex, 4)) {
                Spacer(modifier = GlanceModifier.width(3.dp))
            }
        }
    }
}

private object StreakColors {
    val surface = ColorProvider(day = gray100, night = gray850)
    val primaryText = ColorProvider(day = black, night = white)
    val secondaryText = ColorProvider(day = gray500, night = gray400)
    val lockedBar = ColorProvider(day = gray300, night = gray500)
    val activeDay = ColorProvider(day = hilingualOrange, night = hilingualOrange)
    val inactiveDay = ColorProvider(day = gray200, night = gray500)
    val onActiveDay = ColorProvider(day = white, night = black)
}
