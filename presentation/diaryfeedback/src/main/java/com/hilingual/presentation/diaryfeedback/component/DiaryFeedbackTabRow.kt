package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryFeedbackTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val titles = persistentListOf("문법·철자", "추천표현")

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = HilingualTheme.colors.white,
            contentColor = HilingualTheme.colors.black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = HilingualTheme.colors.black
                )
            },
            divider = {},
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            titles.forEachIndexed { index, title ->
                val selected = (tabIndex == index)
                key(index, title) {
                    Tab(
                        text = {
                            Text(
                                text = title,
                                style = if (selected) HilingualTheme.typography.headB18 else HilingualTheme.typography.headM18
                            )
                        },
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
