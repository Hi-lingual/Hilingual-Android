package com.hilingual.presentation.diaryfeedback.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.diaryfeedback.DiaryFeedbackRoute
import kotlinx.serialization.Serializable

@Serializable
data class DiaryFeedback (
    val diaryId: Long
): Route

fun NavController.navigateToDiaryFeedback(
    diaryId: Long,
    navOptions: NavOptions? = null
) {
    navigate(
        route = DiaryFeedback(diaryId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.diaryFeedbackNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit
) {
    composable<DiaryFeedback> {
        DiaryFeedbackRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )
    }
}