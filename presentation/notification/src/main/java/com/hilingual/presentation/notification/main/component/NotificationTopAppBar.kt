package com.hilingual.presentation.notification.main.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun NotificationTopAppBar(
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        title = "알림",
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24_back),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable(onClick = onBackClick)
            )
        },
        actions = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_setting_24),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable(onClick = onSettingClick)
            )
        }
    )
}

@Preview
@Composable
private fun NotificationTopAppBarPreview() {
    HilingualTheme {
        NotificationTopAppBar(
            onBackClick = {},
            onSettingClick = {}
        )
    }
}
