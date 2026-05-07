package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun NotificationSettingBanner(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = HilingualTheme.colors.gray100, shape = RoundedCornerShape(8.dp))
                .noRippleClickable(onClick = onClick)
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "중요한 알림을 놓치지 마세요!",
                    color = HilingualTheme.colors.black,
                    style = HilingualTheme.typography.bodySB14,
                )

                Text(
                    text = "기기의 알림 설정이 꺼져있어요. 휴대폰 설정 > 알림 > 하이링구얼에서 설정을 변경해 주세요.",
                    color = HilingualTheme.colors.black,
                    style = HilingualTheme.typography.bodyR14,
                )
            }

            Icon(
                imageVector = ImageVector.vectorResource(
                  R.drawable.ic_arrow_right_16_bold,
                ),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400,
            )
        }
    }
}

@Preview
@Composable
private fun NotificationSettingBannerPreview() {
    HilingualTheme {
        NotificationSettingBanner(
            isVisible = true,
            onClick = {},
        )
    }
}
