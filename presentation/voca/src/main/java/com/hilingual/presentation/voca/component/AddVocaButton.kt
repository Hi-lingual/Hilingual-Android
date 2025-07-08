package com.hilingual.presentation.voca.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun AddVocaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(8.dp),
                color = HilingualTheme.colors.black
            )
            .width(IntrinsicSize.Max)
            .padding(12.dp)
            .noRippleClickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "일기 쓰고 단어 추가하기",
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.white
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddVocaButtonPreview() {
    HilingualTheme {
        AddVocaButton(
            onClick = {}
        )
    }
}
