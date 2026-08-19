package com.hilingual.presentation.widget.streak

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
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
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.hilingual.core.designsystem.theme.black
import com.hilingual.core.designsystem.theme.gray100
import com.hilingual.core.designsystem.theme.gray200
import com.hilingual.core.designsystem.theme.gray300
import com.hilingual.core.designsystem.theme.gray400
import com.hilingual.core.designsystem.theme.gray500
import com.hilingual.core.designsystem.theme.gray700
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.designsystem.R as DesignSystemR

private val CompactStreakSize = DpSize(110.dp, 110.dp)
private val WideStreakSize = DpSize(250.dp, 110.dp)

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

@Composable
internal fun StreakWidgetContent(
    state: StreakUiState,
    isWide: Boolean = LocalSize.current.width >= WideStreakSize.width,
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(StreakColors.surface)
            .cornerRadius(28.dp),
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
    Box(modifier = GlanceModifier.fillMaxSize()) {
        Column(modifier = GlanceModifier.fillMaxSize()) {
            Spacer(modifier = GlanceModifier.defaultWeight())
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(StreakColors.ground),
            ) {}
        }
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp),
        ) {
            Text(
                text = "연속 작성",
                style = TextStyle(color = StreakColors.secondaryText, fontSize = 12.sp),
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            if (state.isLoggedIn) {
                StreakValue(state.streakDays, compact = true)
            } else {
                Text(
                    text = "로그인 후 확인 가능",
                    style = TextStyle(
                        color = StreakColors.primaryText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
        Column(modifier = GlanceModifier.fillMaxSize()) {
            Spacer(modifier = GlanceModifier.defaultWeight())
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                StreakCharacter(
                    streakDays = state.streakDays,
                    modifier = GlanceModifier.width(107.dp).height(84.dp),
                )
            }
        }
    }
}

@Composable
private fun WideStreakContent(state: StreakUiState) {
    Row(
        modifier = GlanceModifier.fillMaxSize().padding(20.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxHeight()
                .width(118.dp),
        ) {
            Text(
                text = "연속 작성",
                style = TextStyle(color = StreakColors.secondaryText, fontSize = 14.sp),
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
                        fontSize = 14.sp,
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
        StreakCharacter(
            streakDays = state.streakDays,
            modifier = GlanceModifier.width(130.dp).height(115.dp),
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
            provider = ImageProvider(
                if (compact) {
                    DesignSystemR.drawable.ic_fire_28
                } else {
                    DesignSystemR.drawable.ic_fire_40
                },
            ),
            contentDescription = null,
            modifier = GlanceModifier.size(if (compact) 28.dp else 40.dp),
            colorFilter = if (days == 0) ColorFilter.tint(StreakColors.inactiveFire) else null,
        )
        Text(
            text = days.toString(),
            style = TextStyle(
                color = StreakColors.primaryText,
                fontSize = if (compact) 26.sp else 36.sp,
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
    Row {
        days.take(5).forEachIndexed { index, day ->
            Box(
                modifier = GlanceModifier
                    .size(26.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    provider = ImageProvider(DesignSystemR.drawable.chip_widgetdate),
                    contentDescription = null,
                    modifier = GlanceModifier.size(26.dp),
                    colorFilter = if (enabled && day.isWritten) {
                        null
                    } else {
                        ColorFilter.tint(StreakColors.inactiveDay)
                    },
                )
                Text(
                    text = if (enabled) day.label else "",
                    style = TextStyle(
                        color = if (day.isWritten) StreakColors.onActiveDay else StreakColors.secondaryText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            if (index < minOf(days.lastIndex, 4)) {
                Spacer(modifier = GlanceModifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun StreakCharacter(
    streakDays: Int,
    modifier: GlanceModifier,
) {
    Image(
        provider = ImageProvider(
            if (streakDays == 0) {
                DesignSystemR.drawable.img_widget_4x2_0
            } else {
                DesignSystemR.drawable.img_widget_4x2_n
            },
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

private object StreakColors {
    val surface = ColorProvider(day = gray100, night = gray700)
    val ground = ColorProvider(day = gray200, night = gray500)
    val primaryText = ColorProvider(day = black, night = white)
    val secondaryText = ColorProvider(day = gray500, night = gray400)
    val lockedBar = ColorProvider(day = gray300, night = gray500)
    val inactiveDay = ColorProvider(day = gray200, night = gray500)
    val inactiveFire = ColorProvider(day = gray400, night = gray400)
    val onActiveDay = ColorProvider(day = white, night = black)
}

private class StreakPreviewParameterProvider : PreviewParameterProvider<StreakUiState> {
    override val values = sequenceOf(
        StreakUiState(
            isLoggedIn = false,
            streakDays = 0,
            recentDays = previewDays(),
        ),
        StreakUiState(
            isLoggedIn = true,
            streakDays = 0,
            recentDays = previewDays(),
        ),
        StreakUiState(
            isLoggedIn = true,
            streakDays = 4,
            recentDays = previewDays(setOf("월", "화", "수", "목")),
        ),
    )
}

private fun previewDays(writtenDays: Set<String> = emptySet()) =
    listOf("일", "월", "화", "수", "목").map { day ->
        StreakDay(label = day, isWritten = day in writtenDays)
    }

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakCompactPreview(
    @PreviewParameter(StreakPreviewParameterProvider::class)
    state: StreakUiState,
) {
    StreakWidgetContent(state = state, isWide = false)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakWidePreview(
    @PreviewParameter(StreakPreviewParameterProvider::class)
    state: StreakUiState,
) {
    StreakWidgetContent(state = state, isWide = true)
}
