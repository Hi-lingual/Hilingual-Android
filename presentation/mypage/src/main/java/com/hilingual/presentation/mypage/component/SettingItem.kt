/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.mypage.component

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun SettingItem(
    @DrawableRes iconRes: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.noRippleClickable(onClick = onClick) else Modifier)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = HilingualTheme.colors.gray700
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            color = HilingualTheme.colors.gray700,
            style = HilingualTheme.typography.bodyM14

        )

        Spacer(modifier = Modifier.weight(1f))

        trailingContent()
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
                iconRes = DesignSystemR.drawable.ic_alarm_28,
                title = "알림 설정",
                trailingContent = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp),
                        imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_arrow_right_16_bold),
                        contentDescription = null,
                        tint = HilingualTheme.colors.gray400
                    )
                },
                onClick = { println("SettingItem Clicked in Preview") }
            )
            SettingItem(
                iconRes = DesignSystemR.drawable.ic_info_24,
                title = "버전 정보",
                trailingContent = {
                    Text(
                        text = "1.01.01",
                        color = HilingualTheme.colors.gray400,
                        style = HilingualTheme.typography.captionR14,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            )
            SettingItem(
                iconRes = DesignSystemR.drawable.ic_logout_24,
                title = "로그아웃"
            )
        }
    }
}
