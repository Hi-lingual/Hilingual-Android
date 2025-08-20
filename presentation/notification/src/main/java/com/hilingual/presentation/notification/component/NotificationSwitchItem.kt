package com.hilingual.presentation.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.toggle.HilingualBasicToggleSwitch
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun NotificationSwitchItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HilingualTheme.typography.bodyM16,
            color = HilingualTheme.colors.black
        )
        HilingualBasicToggleSwitch(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
private fun NotificationSwitchItemPreview() {
    var isChecked by remember { mutableStateOf(true) }
    HilingualTheme {
        Column {
            NotificationSwitchItem(
                text = "마케팅 알림",
                isChecked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            NotificationSwitchItem(
                text = "피드 알림",
                isChecked = isChecked,
                onCheckedChange = { isChecked = it }
            )
        }
    }
}
