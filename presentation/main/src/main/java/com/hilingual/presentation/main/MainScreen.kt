package com.hilingual.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                tabs = MainTab.entries.toPersistentList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate
            )
        },
    ) { innerPadding ->
        Column {
            NavHost(
                navController = navigator.navController,
                startDestination = navigator.startDestination,
            ) {
                homeNavGraph(
                    paddingValues = innerPadding
                )
                vocaNavGraph(
                    paddingValues = innerPadding
                )
            }
        }
    }
}