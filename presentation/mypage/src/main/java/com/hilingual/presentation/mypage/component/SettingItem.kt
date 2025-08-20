package com.hilingual.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Immutable
internal data class SettingItemData(
    val iconRes: Int,
    val title: String,
    val trailing: SettingTrailing,
    val onClick: (() -> Unit)?
)

internal sealed class SettingTrailing {
    data object None : SettingTrailing()
    data object Chevron : SettingTrailing()
    data class Text(val value: String) : SettingTrailing()
}

@Composable
internal fun SettingItem(
    data: SettingItemData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(enabled = data.onClick != null, onClick = data.onClick ?: {})
            .background(HilingualTheme.colors.white)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(data.iconRes),
            contentDescription = null,
            tint = HilingualTheme.colors.gray700
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = data.title,
            color = HilingualTheme.colors.gray700,
            style = HilingualTheme.typography.bodyM14

        )

        Spacer(modifier = Modifier.weight(1f))

        when (data.trailing) {
            is SettingTrailing.None -> {}

            is SettingTrailing.Chevron -> Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_arrow_right_16_bold),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )

            is SettingTrailing.Text -> Text(
                text = data.trailing.value,
                color = HilingualTheme.colors.gray400,
                style = HilingualTheme.typography.captionR14,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SettingItemPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingItem(
                data = SettingItemData(
                    iconRes = DesignSystemR.drawable.ic_alarm_28,
                    title = "알림 설정",
                    trailing = SettingTrailing.Chevron,
                    onClick = {}
                )
            )
            SettingItem(
                data = SettingItemData(
                    iconRes = DesignSystemR.drawable.ic_info_24,
                    title = "버전 정보",
                    trailing = SettingTrailing.Text(value = "1.01.01"),
                    onClick = null
                )
            )
            SettingItem(
                data = SettingItemData(
                    iconRes = DesignSystemR.drawable.ic_logout_24,
                    title = "로그아웃",
                    trailing = SettingTrailing.None,
                    onClick = {}
                )
            )
        }
    }
}
