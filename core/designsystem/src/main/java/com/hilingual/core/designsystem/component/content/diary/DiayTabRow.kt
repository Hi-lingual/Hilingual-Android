package com.hilingual.core.designsystem.component.content.diary

import androidx.compose.runtime.Composable
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import kotlinx.collections.immutable.persistentListOf

private val TITLES = persistentListOf("문법·철자", "추천표현")

@Composable
fun DiaryTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    HilingualBasicTabRow(
        tabTitles = TITLES,
        tabIndex = tabIndex,
        onTabSelected = onTabSelected
    )
}