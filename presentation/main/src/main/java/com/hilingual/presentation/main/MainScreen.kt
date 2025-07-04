package com.hilingual.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.hilingual.presentation.home.navigation.homeNavGraph
import com.hilingual.presentation.main.component.MainBottomBar
import com.hilingual.presentation.voca.navigation.vocaNavGraph
import kotlinx.collections.immutable.toPersistentList

private const val EXIT_MILLIS = 3000L

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    val context = LocalContext.current

    var backPressedTime by remember {
        mutableLongStateOf(0L)
    }

    BackHandler {
        if (System.currentTimeMillis() - backPressedTime <= EXIT_MILLIS) {
            (context as Activity).finish()
        } else {
            // TODO: 앱 종료 전 토스트 or 스낵바 표시
        }
        backPressedTime = System.currentTimeMillis()
    }

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