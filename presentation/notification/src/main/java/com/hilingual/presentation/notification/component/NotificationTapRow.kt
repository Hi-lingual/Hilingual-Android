package com.hilingual.presentation.notification.component

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

@Composable
internal fun NotificationTapRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val titles = persistentListOf("피드", "공지사항")

    HilingualBasicTabRow(
        tabTitles = titles,
        tabIndex = tabIndex,
        onTabSelected = onTabSelected,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
internal fun NotificationTapRowPreview() {
    HilingualTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        NotificationTapRow(
            tabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
    }
}
