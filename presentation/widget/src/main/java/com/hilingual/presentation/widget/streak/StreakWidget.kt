package com.hilingual.presentation.widget.streak

import android.content.Context
import androidx.compose.runtime.Composable
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
import com.hilingual.presentation.widget.R
import com.hilingual.presentation.widget.common.WidgetPreviewTheme
import com.hilingual.presentation.widget.common.widgetColorProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import com.hilingual.core.designsystem.R as DesignSystemR

private val WideStreakWidth = 250.dp

class StreakWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            StreakWidgetContent(
                state = StreakUiState(
                    isLoggedIn = true,
                    streakDays = 3,
                    recentDays = StreakUiState.Fake.recentDays,
                ),
            )
        }
    }
}

@Composable
internal fun StreakWidgetContent(
    state: StreakUiState,
    isWide: Boolean = LocalSize.current.width >= WideStreakWidth,
    previewTheme: WidgetPreviewTheme? = null,
) {
    val colors = StreakWidgetColors(previewTheme)
    val size = LocalSize.current
    val contentModifier = if (isWide) {
        GlanceModifier.fillMaxSize()
    } else {
        GlanceModifier.size(minOf(size.width, size.height))
    }

    Box(
        modifier = GlanceModifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = contentModifier
                .background(colors.surface)
                .cornerRadius(28.dp),
        ) {
            if (isWide) {
                StreakLargeWidgetContent(state, colors)
            } else {
                StreakSmallWidgetContent(state, colors)
            }
        }
    }
}

@Composable
private fun StreakSmallWidgetContent(
    state: StreakUiState,
    colors: StreakWidgetColors,
) {
    Box(modifier = GlanceModifier.fillMaxSize()) {
        Column(
            verticalAlignment = Alignment.Bottom,
            modifier = GlanceModifier.fillMaxSize(),
        ) {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(colors.ground),
            ) {
                Image(
                    provider = ImageProvider(R.drawable.bg_widget_ground_line),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colors.groundLine),
                )
            }
        }
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp),
        ) {
            Text(
                text = "연속 작성",
                style = TextStyle(color = colors.secondaryText, fontSize = 12.sp),
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            if (state.isLoggedIn) {
                StreakValue(state.streakDays, compact = true, colors = colors)
            } else {
                Text(
                    text = "로그인 후 확인 가능",
                    style = TextStyle(
                        color = colors.primaryText,
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
                    streakDays = if (state.isLoggedIn) state.streakDays else null,
                    modifier = GlanceModifier.width(107.dp).height(84.dp),
                )
            }
        }
    }
}

@Composable
private fun StreakLargeWidgetContent(
    state: StreakUiState,
    colors: StreakWidgetColors,
) {
    Row(
        modifier = GlanceModifier.fillMaxSize().padding(20.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxHeight()
                .defaultWeight(),
        ) {
            Text(
                text = "연속 작성",
                style = TextStyle(color = colors.secondaryText, fontSize = 14.sp),
            )
            if (state.isLoggedIn) {
                StreakValue(state.streakDays, compact = false, colors = colors)
            } else {
                Spacer(modifier = GlanceModifier.height(5.dp))
                Box(
                    modifier = GlanceModifier
                        .width(72.dp)
                        .height(18.dp)
                        .background(colors.lockedBar)
                        .cornerRadius(3.dp),
                ) {}
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = "로그인 후 확인 가능",
                    style = TextStyle(
                        color = colors.primaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            Spacer(modifier = GlanceModifier.defaultWeight())
            RecentDays(
                isLoggedIn = state.isLoggedIn,
                days = state.recentDays,
                enabled = state.isLoggedIn,
                colors = colors,
            )
        }
        StreakCharacter(
            streakDays = if (state.isLoggedIn) state.streakDays else null,
            modifier = GlanceModifier.width(130.dp).height(115.dp),
        )
    }
}

@Composable
private fun StreakValue(
    days: Int,
    compact: Boolean,
    colors: StreakWidgetColors,
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
            colorFilter = if (days == 0) ColorFilter.tint(colors.inactiveFire) else null,
        )
        Text(
            text = days.toString(),
            style = TextStyle(
                color = colors.primaryText,
                fontSize = if (compact) 26.sp else 36.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun RecentDays(
    isLoggedIn: Boolean,
    days: ImmutableList<StreakDay>,
    enabled: Boolean,
    colors: StreakWidgetColors,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        modifier = modifier,
    ) {
        days.take(5).forEachIndexed { index, day ->
            if (isLoggedIn) {
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
                            ColorFilter.tint(colors.inactiveDay)
                        },
                    )
                    Text(
                        text = if (enabled) convertDateToDayOfWeek(day.date) else "",
                        style = TextStyle(
                            color = if (day.isWritten) colors.onActiveDay else colors.secondaryText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            } else {
                Image(
                    provider = ImageProvider(DesignSystemR.drawable.chip_widgetdate_skeleton),
                    contentDescription = null,
                    modifier = GlanceModifier.size(26.dp),
                    colorFilter = ColorFilter.tint(colors.inactiveDay),
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
    streakDays: Int?,
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

private fun convertDateToDayOfWeek(date: LocalDate): String = date.format(
    DateTimeFormatter.ofPattern("E", Locale.KOREAN),
)

private class StreakWidgetColors(previewTheme: WidgetPreviewTheme?) {
    val surface = widgetColorProvider(gray100, gray700, previewTheme)
    val ground = widgetColorProvider(gray200, gray500, previewTheme)
    val groundLine = widgetColorProvider(gray500, black, previewTheme)
    val primaryText = widgetColorProvider(black, white, previewTheme)
    val secondaryText = widgetColorProvider(gray500, gray200, previewTheme)
    val lockedBar = widgetColorProvider(gray300, gray500, previewTheme)
    val inactiveDay = widgetColorProvider(gray200, gray500, previewTheme)
    val inactiveFire = widgetColorProvider(gray400, gray300, previewTheme)
    val onActiveDay = widgetColorProvider(white, white, previewTheme)
}

@Composable
private fun StreakPreview(
    state: StreakUiState,
    isWide: Boolean,
    theme: WidgetPreviewTheme,
) {
    StreakWidgetContent(
        state = state,
        isWide = isWide,
        previewTheme = theme,
    )
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakLoggedOutLargeLightPreview() {
    StreakPreview(StreakUiState.LoggedOutFake, true, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakLoggedOutSmallLightPreview() {
    StreakPreview(StreakUiState.LoggedOutFake, false, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakLoggedOutLargeDarkPreview() {
    StreakPreview(StreakUiState.LoggedOutFake, true, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakLoggedOutSmallDarkPreview() {
    StreakPreview(StreakUiState.LoggedOutFake, false, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakEmptyLargeLightPreview() {
    StreakPreview(StreakUiState.EmptyFake, true, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakEmptySmallLightPreview() {
    StreakPreview(StreakUiState.EmptyFake, false, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakEmptyLargeDarkPreview() {
    StreakPreview(StreakUiState.EmptyFake, true, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakEmptySmallDarkPreview() {
    StreakPreview(StreakUiState.EmptyFake, false, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakWrittenLargeLightPreview() {
    StreakPreview(StreakUiState.Fake, true, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakWrittenSmallLightPreview() {
    StreakPreview(StreakUiState.Fake, false, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakWrittenLargeDarkPreview() {
    StreakPreview(StreakUiState.Fake, true, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakWrittenSmallDarkPreview() {
    StreakPreview(StreakUiState.Fake, false, WidgetPreviewTheme.DARK)
}
