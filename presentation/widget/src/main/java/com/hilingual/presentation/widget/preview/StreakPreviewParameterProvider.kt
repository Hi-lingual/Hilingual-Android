package com.hilingual.presentation.widget.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hilingual.presentation.widget.StreakDay
import com.hilingual.presentation.widget.StreakUiState
import com.hilingual.presentation.widget.StreakWidgetContent

internal class StreakPreviewParameterProvider : PreviewParameterProvider<StreakUiState> {
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
            recentDays = previewDays(
                writtenDays = setOf("월", "화", "수", "목"),
            ),
        ),
    )
}

private fun previewDays(writtenDays: Set<String> = emptySet()) =
    listOf("일", "월", "화", "수", "목").map { day ->
        StreakDay(
            label = day,
            isWritten = day in writtenDays,
        )
    }

@Preview(
    name = "Streak 2x2 Light",
    widthDp = 155,
    heightDp = 155,
)
@Preview(
    name = "Streak 2x2 Dark",
    widthDp = 155,
    heightDp = 155,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun StreakCompactPreview(
    @PreviewParameter(StreakPreviewParameterProvider::class)
    state: StreakUiState,
) {
    StreakWidgetContent(
        state = state,
        isWide = false,
    )
}

@Preview(
    name = "Streak 4x2 Light",
    widthDp = 329,
    heightDp = 155,
)
@Preview(
    name = "Streak 4x2 Dark",
    widthDp = 329,
    heightDp = 155,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun StreakWidePreview(
    @PreviewParameter(StreakPreviewParameterProvider::class)
    state: StreakUiState,
) {
    StreakWidgetContent(
        state = state,
        isWide = true,
    )
}
