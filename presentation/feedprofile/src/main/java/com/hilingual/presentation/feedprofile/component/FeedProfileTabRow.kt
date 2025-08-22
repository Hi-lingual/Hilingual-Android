package com.hilingual.presentation.feedprofile.component

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

private val TITLES = persistentListOf("공유한 일기", "공감한 일기")

@Composable
internal fun FeedProfileTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicTabRow(
        tabTitles = TITLES,
        tabIndex = tabIndex,
        onTabSelected = onTabSelected
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedProfileTapRowPreview() {
    HilingualTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        FeedProfileTabRow(
            tabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
    }
}