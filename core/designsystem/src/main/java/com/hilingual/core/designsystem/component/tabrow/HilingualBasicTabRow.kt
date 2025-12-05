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
package com.hilingual.core.designsystem.component.tabrow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HilingualBasicTabRow(
    tabTitles: ImmutableList<String>,
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        SecondaryTabRow(
            selectedTabIndex = tabIndex,
            containerColor = HilingualTheme.colors.white,
            contentColor = HilingualTheme.colors.black,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabIndex),
                    color = HilingualTheme.colors.black
                )
            },
            divider = {},
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            tabTitles.forEachIndexed { index, title ->
                val selected = (tabIndex == index)
                key(title) {
                    Tab(
                        text = {
                            Text(
                                text = title,
                                style = if (selected) HilingualTheme.typography.headSB18 else HilingualTheme.typography.headR18,
                                textAlign = TextAlign.Center
                            )
                        },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(),
                        selected = selected,
                        onClick = { onTabSelected(index) },
                        selectedContentColor = HilingualTheme.colors.black,
                        unselectedContentColor = HilingualTheme.colors.gray500
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TabRowPreview() {
    HilingualTheme {
        val pagerState1 = rememberPagerState(pageCount = { 2 })
        val pagerState2 = rememberPagerState(pageCount = { 3 })

        Column {
            HilingualBasicTabRow(
                tabTitles = persistentListOf("문법·철자", "추천표현"),
                tabIndex = pagerState1.currentPage,
                onTabSelected = {}
            )

            HilingualBasicTabRow(
                tabTitles = persistentListOf("tab1", "tab2", "tab3"),
                tabIndex = pagerState2.currentPage,
                onTabSelected = {}
            )
        }
    }
}
