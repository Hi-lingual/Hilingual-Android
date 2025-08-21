package com.hilingual.presentation.notification.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.setting.component.NotificationSwitchItem

@Composable
internal fun NotificationSettingScreen(
    isMarketingChecked: Boolean,
    onMarketingCheckedChange: (Boolean) -> Unit,
    isFeedChecked: Boolean,
    onFeedCheckedChange: (Boolean) -> Unit,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "알림 설정",
            onBackClicked = onBackClick
        )

        NotificationSwitchItem(
            text = "마케팅 알림",
            isChecked = isMarketingChecked,
            onCheckedChange = onMarketingCheckedChange
        )

        NotificationSwitchItem(
            text = "피드 알림",
            isChecked = isFeedChecked,
            onCheckedChange = onFeedCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationSettingScreenPreview() {
    var isMarketingChecked by remember { mutableStateOf(true) }
    var isFeedChecked by remember { mutableStateOf(false) }

    HilingualTheme {
        NotificationSettingScreen(
            isMarketingChecked = isMarketingChecked,
            onMarketingCheckedChange = { isMarketingChecked = it },
            isFeedChecked = isFeedChecked,
            onFeedCheckedChange = { isFeedChecked = it },
            paddingValues = PaddingValues(0.dp),
            onBackClick = {}
        )
    }
}
