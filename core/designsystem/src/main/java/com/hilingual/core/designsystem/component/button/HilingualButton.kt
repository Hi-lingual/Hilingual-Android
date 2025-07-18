package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableProvider: () -> Boolean = { true }
) {
    val enabled = enableProvider()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (enabled) HilingualTheme.colors.black else HilingualTheme.colors.gray300
            )
            .noRippleClickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(vertical = 18.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = HilingualTheme.colors.white,
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
            enableProvider = { false },
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonTogglePreview() {
    HilingualTheme {
        var isEnabled by remember { mutableStateOf(true) }

        HilingualButton(
            text = if (isEnabled) "활성화됨" else "비활성화됨",
            enableProvider = { isEnabled },
            onClick = { isEnabled = !isEnabled }
        )
    }
}
