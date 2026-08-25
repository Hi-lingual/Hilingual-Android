package com.hilingual.presentation.widget.streak

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
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
import com.hilingual.data.widget.repository.WidgetRepository
import com.hilingual.presentation.widget.R
import com.hilingual.presentation.widget.common.WidgetPreviewTheme
import com.hilingual.presentation.widget.common.homeLaunchAction
import com.hilingual.presentation.widget.common.widgetColorProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import com.hilingual.core.designsystem.R as DesignSystemR
import java.time.format.TextStyle as JavaTextStyle

private val SmallStreakPreviewSize = DpSize(155.dp, 155.dp)
private val LargeStreakPreviewSize = DpSize(329.dp, 155.dp)
private val WideStreakWidth = 250.dp
private val LargeWidgetHorizontalPadding = 20.dp
private val LargeWidgetCharacterWidth = 130.dp
private val RecentDaySize = 26.dp
private val MinimumRecentDaySize = 24.dp
private val RecentDaySpacing = 6.dp
private const val MAX_RECENT_DAY_COUNT = 5

class StreakWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact
    override val previewSizeMode = SizeMode.Responsive(
        setOf(SmallStreakPreviewSize, LargeStreakPreviewSize),
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val state = loadState(context)

        provideContent {
            StreakWidgetContent(
                state = state,
                launchAction = homeLaunchAction(context),
            )
        }
    }

    override suspend fun providePreview(
        context: Context,
        widgetCategory: Int,
    ) {
        val state = loadState(context)

        provideContent {
            StreakWidgetContent(state = state)
        }
    }

    private suspend fun loadState(context: Context): StreakUiState {
        val repository = EntryPointAccessors.fromApplication(
            context.applicationContext,
            StreakWidgetEntryPoint::class.java,
        ).widgetRepository()

        return if (repository.isLoggedIn()) {
            repository.getStreak(LocalDate.now())
                .map(StreakUiState::from)
                .getOrElse { StreakUiState.Empty }
        } else {
            StreakUiState()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface StreakWidgetEntryPoint {
    fun widgetRepository(): WidgetRepository
}

@Composable
internal fun StreakWidgetContent(
    state: StreakUiState,
    isWide: Boolean = LocalSize.current.width >= WideStreakWidth,
    previewTheme: WidgetPreviewTheme? = null,
    launchAction: Action? = null,
) {
    val colors = StreakWidgetColors(previewTheme)
    val isDarkTheme = when (previewTheme) {
        WidgetPreviewTheme.LIGHT -> false

        WidgetPreviewTheme.DARK -> true

        null ->
            LocalContext.current.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    val groundImageProvider = ImageProvider(
        if (isDarkTheme) R.drawable.bg_streak_ground_dark else R.drawable.bg_streak_ground_light,
    )
    val size = LocalSize.current
    val contentModifier = if (isWide) {
        GlanceModifier.fillMaxSize()
    } else {
        GlanceModifier.size(minOf(size.width, size.height))
    }

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .then(if (launchAction != null) GlanceModifier.clickable(launchAction) else GlanceModifier),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = contentModifier
                .background(colors.surface)
                .appWidgetBackground()
                .cornerRadius(android.R.dimen.system_app_widget_background_radius),
        ) {
            if (isWide) {
                StreakLargeWidgetContent(state, colors)
            } else {
                StreakSmallWidgetContent(state, colors, groundImageProvider)
            }
        }
    }
}

@Composable
private fun StreakSmallWidgetContent(
    state: StreakUiState,
    colors: StreakWidgetColors,
    groundImageProvider: ImageProvider,
) {
    Box(modifier = GlanceModifier.fillMaxSize()) {
        Column(
            verticalAlignment = Alignment.Bottom,
            modifier = GlanceModifier.fillMaxSize(),
        ) {
            Image(
                provider = groundImageProvider,
                contentDescription = null,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(35.dp),
                contentScale = ContentScale.FillBounds,
            )
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
    val availableDaysWidth = LocalSize.current.width -
        (LargeWidgetHorizontalPadding * 2) -
        (LargeWidgetCharacterWidth + 5.dp)
    val visibleDayCount = (
        (availableDaysWidth.value + RecentDaySpacing.value) /
            (MinimumRecentDaySize.value + RecentDaySpacing.value)
        ).toInt().coerceIn(1, MAX_RECENT_DAY_COUNT)
    val recentDaySize = (
        (availableDaysWidth.value - RecentDaySpacing.value * (visibleDayCount - 1)) /
            visibleDayCount
        ).dp

    Row(
        modifier = GlanceModifier.fillMaxSize().padding(LargeWidgetHorizontalPadding),
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
                maxVisibleDays = visibleDayCount,
                daySize = recentDaySize,
            )
        }
        Spacer(modifier = GlanceModifier.width(5.dp))
        StreakCharacter(
            streakDays = if (state.isLoggedIn) state.streakDays else null,
            modifier = GlanceModifier.width(LargeWidgetCharacterWidth).height(115.dp),
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
    maxVisibleDays: Int = MAX_RECENT_DAY_COUNT,
    daySize: Dp = RecentDaySize,
) {
    val visibleDays = days.takeLast(maxVisibleDays)
    val dayFontSize = (12f * daySize.value / RecentDaySize.value).sp

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        visibleDays.forEachIndexed { index, day ->
            if (isLoggedIn) {
                Box(
                    modifier = GlanceModifier
                        .size(daySize),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        provider = ImageProvider(DesignSystemR.drawable.chip_widgetdate),
                        contentDescription = null,
                        modifier = GlanceModifier.size(daySize),
                        colorFilter = if (enabled && day.isWritten) {
                            null
                        } else {
                            ColorFilter.tint(colors.inactiveDay)
                        },
                    )
                    Text(
                        text = if (enabled) day.dayOfWeek.toKoreanShortName() else "",
                        style = TextStyle(
                            color = if (day.isWritten) colors.onActiveDay else colors.secondaryText,
                            fontSize = dayFontSize,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            } else {
                Image(
                    provider = ImageProvider(DesignSystemR.drawable.chip_widgetdate_skeleton),
                    contentDescription = null,
                    modifier = GlanceModifier.size(daySize),
                    colorFilter = ColorFilter.tint(colors.inactiveDay),
                )
            }
            if (index < visibleDays.lastIndex) {
                Spacer(modifier = GlanceModifier.width(RecentDaySpacing))
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

private fun DayOfWeek.toKoreanShortName(): String = getDisplayName(JavaTextStyle.SHORT, Locale.KOREAN)

private class StreakWidgetColors(previewTheme: WidgetPreviewTheme?) {
    val surface = widgetColorProvider(gray100, gray700, previewTheme)
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
    StreakPreview(StreakUiState(), true, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakLoggedOutSmallLightPreview() {
    StreakPreview(StreakUiState(), false, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakLoggedOutLargeDarkPreview() {
    StreakPreview(StreakUiState(), true, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakLoggedOutSmallDarkPreview() {
    StreakPreview(StreakUiState(), false, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakEmptyLargeLightPreview() {
    StreakPreview(StreakUiState.Empty, true, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakEmptySmallLightPreview() {
    StreakPreview(StreakUiState.Empty, false, WidgetPreviewTheme.LIGHT)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 329, heightDp = 155)
@Composable
private fun StreakEmptyLargeDarkPreview() {
    StreakPreview(StreakUiState.Empty, true, WidgetPreviewTheme.DARK)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 155, heightDp = 155)
@Composable
private fun StreakEmptySmallDarkPreview() {
    StreakPreview(StreakUiState.Empty, false, WidgetPreviewTheme.DARK)
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
