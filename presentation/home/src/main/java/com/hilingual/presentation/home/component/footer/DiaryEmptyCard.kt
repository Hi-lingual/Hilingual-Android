package com.hilingual.presentation.home.component.footer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R

enum class DiaryEmptyCardType(
    val text: String,
    @DrawableRes val imageRes: Int
) {
    PAST(
        text = "작성된 일기가 없어요\n좋은 하루 보내셨기를 바라요!",
        imageRes = R.drawable.img_diary_empty
    ),
    FUTURE(
        text = "아직 작성 가능한 시간이 아니에요.\n오늘의 일기를 작성해주세요!",
        imageRes = R.drawable.img_diary_lock
    )
}

@Composable
internal fun DiaryEmptyCard(
    type: DiaryEmptyCardType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = type.imageRes),
            contentDescription = null
        )
        Text(
            text = type.text,
            style = HilingualTheme.typography.bodySB14,
            color = HilingualTheme.colors.gray400,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

private class DiaryEmptyCardPreviewParameterProvider : PreviewParameterProvider<DiaryEmptyCardType> {
    override val values: Sequence<DiaryEmptyCardType>
        get() = DiaryEmptyCardType.entries.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun DiaryEmptyCardPreview(
    @PreviewParameter(DiaryEmptyCardPreviewParameterProvider::class) type: DiaryEmptyCardType
) {
    HilingualTheme {
        DiaryEmptyCard(type = type)
    }
}
