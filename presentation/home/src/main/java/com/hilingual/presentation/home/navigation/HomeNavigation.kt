package com.hilingual.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.presentation.home.HomeRoute
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data object Home : MainTabRoute

fun NavController.navigateToHome(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Home,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    navigateToDiaryWrite: (selectedDate: LocalDate) -> Unit
) {
    composable<Home> {
        HomeRoute(
            paddingValues = paddingValues,
            navigateToDiaryFeedback = navigateToDiaryFeedback,
            navigateToDiaryWrite = navigateToDiaryWrite
        )
    }
}
