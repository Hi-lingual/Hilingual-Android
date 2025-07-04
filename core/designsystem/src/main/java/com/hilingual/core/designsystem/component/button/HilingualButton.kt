package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.coroutines.delay

@Composable
fun HilingualButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val backgroundColor = if (!enabled) {
        HilingualTheme.colors.gray300
    } else {
        HilingualTheme.colors.black
    }
    val textColor = HilingualTheme.colors.white
    Box(
        modifier = modifier
            .width(328.dp)
            .height(58.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable {
                if (enabled) onClick()
            }
            .padding(vertical = 18.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = HilingualTheme.typography.bodySB16
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnabledButtonPreview() {
    HilingualTheme {
        HilingualButton(
            text = "시작하기",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisabledButtonPreview() {
    HilingualTheme {
        HilingualButton(
            text = "시작하기",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonTogglePreview() {
    HilingualTheme {
        var isEnabled by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(2000)
            isEnabled = true
        }

        HilingualButton(
            text = if (isEnabled) "활성화됨" else "비활성화됨",
            enabled = isEnabled,
            onClick = {}
        )
    }
}
