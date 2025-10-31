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
package com.hilingual.presentation.feedprofile.follow.component.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.persistentListOf

private val TITLES = persistentListOf("팔로워", "팔로잉")

@Composable
internal fun FollowTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicTabRow(
        tabTitles = TITLES,
        tabIndex = tabIndex,
        onTabSelected = onTabSelected,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun FollowTapRowPreview() {
    HilingualTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        FollowTabRow(
            tabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
    }
}
