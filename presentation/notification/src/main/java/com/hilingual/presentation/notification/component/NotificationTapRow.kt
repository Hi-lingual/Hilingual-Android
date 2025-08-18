package com.hilingual.presentation.notification.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun NotificationTapRow(
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val titles = persistentListOf("피드", "공지사항")
    HilingualBasicTabRow(
        tabTitles = titles,
        tabIndex = titles.size,
        onTabSelected = onTabSelected,
        modifier = modifier
    )
}
