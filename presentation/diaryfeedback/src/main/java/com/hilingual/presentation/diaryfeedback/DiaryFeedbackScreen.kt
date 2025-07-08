package com.hilingual.presentation.diaryfeedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diaryfeedback.tab.GrammarSpellingScreen
import com.hilingual.presentation.diaryfeedback.tab.RecommendExpressionScreen
import kotlinx.collections.immutable.toImmutableList

//TODO: Route 추가

@Composable
private fun DiaryFeedbackScreen(
    modifier: Modifier = Modifier
) {
    val titles = listOf("문법·철자", "추천표현").toImmutableList()
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
    ) {
        BackAndMoreTopAppBar(
            title = "일기장",
            onBackClicked = {
                //TODO: 뒤로가기 동작 추가
            },
            onMoreClicked = {
                //TODO: AI 신고하기 바텀시트 노출
            }
        )
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
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            titles.forEachIndexed { index, title ->
                val selected = ( tabIndex == index )
                Tab(
                    text = { Text(
                        text = title,
                        style = if (selected) HilingualTheme.typography.headB18 else HilingualTheme.typography.headM18
                    ) },
                    selected = selected,
                    onClick = { tabIndex = index },
                    selectedContentColor = HilingualTheme.colors.black,
                    unselectedContentColor = HilingualTheme.colors.gray500,
                )
            }
        }
        if (tabIndex == 0) {
            GrammarSpellingScreen() // 문법·철자 화면
        } else {
            RecommendExpressionScreen() // 추천 표현 화면
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryFeedbackPreview() {
    HilingualTheme {
        DiaryFeedbackScreen()
    }
}