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
package com.hilingual.presentation.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.main.MainTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Column(
            modifier = Modifier
                .background(HilingualTheme.colors.white)
        ) {
            HorizontalDivider(
                color = HilingualTheme.colors.gray100,
                thickness = 1.dp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            ) {
                tabs.forEach { tab ->
                    key(tab.route) {
                        MainBottomBarItem(
                            tab = tab,
                            selected = (tab == currentTab),
                            onClick = { onTabSelected(tab) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .noRippleClickable(onClick = onClick)
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(tab.iconRes),
            modifier = Modifier.size(24.dp),
            contentDescription = tab.label,
            tint = if (selected) {
                HilingualTheme.colors.hilingualBlack
            } else {
                HilingualTheme.colors.gray200
            }
        )
        Text(
            text = tab.label,
            color = if (selected) {
                HilingualTheme.colors.hilingualBlack
            } else {
                HilingualTheme.colors.gray200
            },
            style = HilingualTheme.typography.captionR12
        )
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    HilingualTheme {
        Column(modifier = Modifier) {
            MainBottomBar(
                visible = true,
                tabs = MainTab.entries.toPersistentList(),
                currentTab = MainTab.HOME,
                onTabSelected = {}
            )
        }
    }
}
