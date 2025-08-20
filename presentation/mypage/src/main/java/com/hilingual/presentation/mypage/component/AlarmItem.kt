package com.hilingual.presentation.mypage.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
internal fun AlarmItem(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = HilingualTheme.colors.black,
            style = HilingualTheme.typography.bodyM16

        )

        Spacer(modifier = Modifier.weight(1f))

        HilingualBasicToggleSwitch(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmItemPreview() {
    var isMarketingAlarmChecked by remember { mutableStateOf(true) }
    var isfeedAlarmChecked by remember { mutableStateOf(false) }

    HilingualTheme {
        Column {
            AlarmItem(
                label = "마케팅 알림",
                isChecked = isMarketingAlarmChecked,
                onCheckedChange = { isMarketingAlarmChecked = it }
            )
            AlarmItem(
                label = "피드 알림",
                isChecked = isfeedAlarmChecked,
                onCheckedChange = { isfeedAlarmChecked = it }
            )
        }
    }
}
