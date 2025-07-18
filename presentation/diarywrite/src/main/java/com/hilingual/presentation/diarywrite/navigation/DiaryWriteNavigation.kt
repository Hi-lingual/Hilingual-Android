package com.hilingual.presentation.diarywrite.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.diarywrite.DiaryWriteRoute
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DiaryWrite(
    val selectedDate: String
) : Route

fun NavController.navigateToDiaryWrite(
    selectedDate: LocalDate,
    navOptions: NavOptions? = null
) {
    navigate(
        route = DiaryWrite(selectedDate.toString()),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.diaryWriteNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit
) {
    composable<DiaryWrite> {
        DiaryWriteRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToHome = navigateToHome,
            navigateToDiaryFeedback = navigateToDiaryFeedback
        )
    }
}
