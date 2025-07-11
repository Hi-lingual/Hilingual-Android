package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R

@Composable
internal fun DateTimeInfo(
    isWritten: Boolean,
    writtenTime: String?,
    remainingTime: Int?,
    modifier: Modifier = Modifier
) {
    if (isWritten) {
        Text(
            text = writtenTime ?: "",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray300,
            modifier = modifier
        )
    } else if (remainingTime != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_time_16),
                contentDescription = "남은 시간",
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(4.dp))

            val annotatedString = buildAnnotatedString {
                val (timeValue, timeUnit) = calculateRemainingTime(remainingTime)

                withStyle(style = SpanStyle(color = HilingualTheme.colors.hilingualOrange)) {
                    append(timeValue.toString())
                }
                withStyle(style = SpanStyle(color = HilingualTheme.colors.black)) {
                    append("$timeUnit 남았어요")
                }
            }
            Text(
                text = annotatedString,
                style = HilingualTheme.typography.bodySB14
            )
        }
    }
}

private fun calculateRemainingTime(remainingTime: Int): Pair<Int, String> =
    when {
        remainingTime >= 60 -> (remainingTime / 60) to "시간"
        remainingTime >= 1 -> remainingTime to "분"
        else -> 1 to "분"
    }

private data class DateTimeInfoPreviewState(
    val isWritten: Boolean,
    val writtenTime: String?,
    val remainingTime: Int?
)

private class DateTimeInfoPreviewProvider :
    PreviewParameterProvider<DateTimeInfoPreviewState> {
    override val values = sequenceOf(
        DateTimeInfoPreviewState(isWritten = false, writtenTime = null, remainingTime = 1440),
        DateTimeInfoPreviewState(isWritten = true, writtenTime = "14:30", remainingTime = null)
    )
}

@Preview(showBackground = true)
@Composable
private fun DateTimeInfoPreview(
    @PreviewParameter(DateTimeInfoPreviewProvider::class) state: DateTimeInfoPreviewState
) {
    HilingualTheme {
        DateTimeInfo(
            isWritten = state.isWritten,
            writtenTime = state.writtenTime,
            remainingTime = state.remainingTime
        )
    }
}
