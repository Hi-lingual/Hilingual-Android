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
