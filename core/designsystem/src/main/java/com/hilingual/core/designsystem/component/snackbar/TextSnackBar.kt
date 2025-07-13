package com.hilingual.core.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TextSnackBar(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = HilingualTheme.colors.gray700,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 8.dp,
                horizontal = 24.dp
            ),
        textAlign = TextAlign.Center,
        style = HilingualTheme.typography.bodyM16,
        color = HilingualTheme.colors.white
    )
}
