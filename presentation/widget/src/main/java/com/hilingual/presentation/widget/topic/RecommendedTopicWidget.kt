package com.hilingual.presentation.widget.topic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
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
import com.hilingual.core.common.util.toKoreanShortWeekdayDate
import com.hilingual.core.common.widget.WidgetType
import com.hilingual.presentation.widget.common.WidgetEntryPoint
import com.hilingual.presentation.widget.common.WidgetPreviewTheme
import com.hilingual.presentation.widget.common.WidgetPreviews
import com.hilingual.presentation.widget.common.homeLaunchAction
import dagger.hilt.android.EntryPointAccessors
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import com.hilingual.core.designsystem.R as DesignSystemR

private val WideTopicWidth = 250.dp

class RecommendedTopicWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact
    override val previewSizeMode = SizeMode.Single

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val state = loadState(context)

        provideContent {
            RecommendedTopicWidgetContent(
                state = state,
                launchAction = homeLaunchAction(context, WidgetType.DIARY_TOPIC),
            )
        }
    }

    override suspend fun providePreview(
        context: Context,
        widgetCategory: Int,
    ) {
        val state = loadState(context)

        provideContent {
            RecommendedTopicWidgetContent(state = state)
        }
    }

    private suspend fun loadState(context: Context): RecommendedTopicUiState {
        val repository = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java,
        ).widgetRepository()

        return repository.getTopic(LocalDate.now())
            .map(RecommendedTopicUiState::from)
            .getOrElse { RecommendedTopicUiState.unavailable() }
    }
}

@Composable
internal fun RecommendedTopicWidgetContent(
    state: RecommendedTopicUiState,
    isWide: Boolean = LocalSize.current.width >= WideTopicWidth,
    previewTheme: WidgetPreviewTheme? = null,
    launchAction: Action? = null,
) {
    val colors = RecommendedTopicWidgetColors(previewTheme)
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
        Column(
            modifier = contentModifier
                .background(colors.surface)
                .appWidgetBackground()
                .cornerRadius(android.R.dimen.system_app_widget_background_radius),
        ) {
            if (isWide) {
                LargeTopicHeader(state.date, state.writingStatus, colors)
                LargeTopicBody(state.topic, colors)
            } else {
                SmallTopicHeader(state.date, colors)
                SmallTopicBody(state.topic, state.writingStatus, colors)
            }
        }
    }
}

@Composable
private fun SmallTopicHeader(
    date: LocalDate,
    colors: RecommendedTopicWidgetColors,
) {
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(40.dp)
            .background(colors.header)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = date.toKoreanShortWeekdayDate(),
            style = TextStyle(
                color = colors.onHeader,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun SmallTopicBody(
    topicEn: String?,
    writingStatus: WritingStatus,
    colors: RecommendedTopicWidgetColors,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = topicEn ?: "지금은 주제를\n불러올 수 없어요",
            modifier = GlanceModifier.defaultWeight(),
            style = TextStyle(
                color = colors.primaryText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 3,
        )
        if (writingStatus == WritingStatus.UNWRITTEN) {
            RemainingTime(isWide = false, colors = colors)
        }
    }
}

@Composable
private fun LargeTopicHeader(
    date: LocalDate,
    writingStatus: WritingStatus,
    colors: RecommendedTopicWidgetColors,
) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colors.header)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date.toKoreanShortWeekdayDate(),
            style = TextStyle(
                color = colors.onHeader,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
        if (writingStatus != WritingStatus.UNKNOWN) {
            Spacer(modifier = GlanceModifier.width(5.dp))
            Text(
                text = "·",
                style = TextStyle(color = colors.onHeaderMuted, fontSize = 13.sp),
            )
            Spacer(modifier = GlanceModifier.width(5.dp))
            Text(
                text = if (writingStatus == WritingStatus.WRITTEN) "작성완료" else "미작성",
                style = TextStyle(
                    color = if (writingStatus == WritingStatus.WRITTEN) {
                        colors.accent
                    } else {
                        colors.onHeaderMuted
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        if (writingStatus == WritingStatus.UNWRITTEN) {
            RemainingTime(isWide = true, colors = colors)
        }
    }
}

@Composable
private fun LargeTopicBody(
    topicEn: String?,
    colors: RecommendedTopicWidgetColors,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "오늘의 추천 주제",
            style = TextStyle(color = colors.secondaryText, fontSize = 11.sp),
        )
        Spacer(modifier = GlanceModifier.height(5.dp))
        Text(
            text = topicEn ?: "지금은 주제를 불러올 수 없어요",
            style = TextStyle(
                color = colors.primaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 2,
        )
    }
}

@Composable
private fun RemainingTime(
    isWide: Boolean,
    colors: RecommendedTopicWidgetColors,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(DesignSystemR.drawable.ic_time_16),
            contentDescription = null,
            modifier = GlanceModifier.size(16.dp),
        )
        Spacer(modifier = GlanceModifier.width(2.dp))
        Text(
            text = "${calculateRemainingHours(LocalDate.now())}시간 남음",
            style = TextStyle(
                color = if (isWide) colors.leftHourLargeText else colors.leftHourSmallText,
                fontSize = 12.sp,
            ),
        )
    }
}

private fun calculateRemainingHours(date: LocalDate): Int {
    val now = ZonedDateTime.now()
    val deadline = date.plusDays(2).atStartOfDay(now.zone)
    return Duration.between(now, deadline).toHours().toInt().coerceAtLeast(0)
}

@Composable
private fun RecommendedTopicPreview(
    state: RecommendedTopicUiState,
    theme: WidgetPreviewTheme,
) = RecommendedTopicWidgetContent(
    state = state,
    previewTheme = theme,
)

@WidgetPreviews
@Composable
private fun RecommendedTopicUnwrittenLightPreview() =
    RecommendedTopicPreview(RecommendedTopicUiState.Fake, WidgetPreviewTheme.LIGHT)

@WidgetPreviews
@Composable
private fun RecommendedTopicUnwrittenDarkPreview() =
    RecommendedTopicPreview(RecommendedTopicUiState.Fake, WidgetPreviewTheme.DARK)

@WidgetPreviews
@Composable
private fun RecommendedTopicWrittenLightPreview() =
    RecommendedTopicPreview(RecommendedTopicUiState.WrittenFake, WidgetPreviewTheme.LIGHT)

@WidgetPreviews
@Composable
private fun RecommendedTopicWrittenDarkPreview() =
    RecommendedTopicPreview(RecommendedTopicUiState.WrittenFake, WidgetPreviewTheme.DARK)
