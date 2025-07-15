package com.hilingual.presentation.diarywrite.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.diarywrite.DiaryWriteRoute
import kotlinx.serialization.Serializable

@Serializable
data object DiaryWrite : Route

fun NavController.navigateToDiaryWrite(
    navOptions: NavOptions? = null
) {
    navigate(
        route = DiaryWrite,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.diaryWriteNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit
) {
    composable<DiaryWrite> {
        DiaryWriteRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToHome = {},
            navigateToDiaryFeedback = {}
        )
    }
}
