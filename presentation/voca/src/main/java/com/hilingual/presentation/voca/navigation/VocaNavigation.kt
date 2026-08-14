/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.voca.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.voca.VocaRoute
import com.hilingual.presentation.voca.review.VocaReviewRoute
import kotlinx.serialization.Serializable

private const val VOCA_REVIEW_SAVED_KEY = "voca_review_saved"

@Serializable
data object Voca : MainTabRoute

@Serializable
data class VocaReview(
    val unmemorizedOnly: Boolean,
    val sort: Int,
) : Route

fun NavController.navigateToVoca(
    navOptions: NavOptions? = null,
) {
    navigate(
        route = Voca,
        navOptions = navOptions,
    )
}

fun NavController.navigateToVocaReview(
    unmemorizedOnly: Boolean,
    sort: Int,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = VocaReview(unmemorizedOnly = unmemorizedOnly, sort = sort),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.vocaNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateToHome: () -> Unit,
) {
    composable<Voca> { entry ->
        VocaRoute(
            paddingValues = paddingValues,
            navigateToHome = navigateToHome,
            navigateToVocaReview = { unmemorizedOnly, sort ->
                navController.navigateToVocaReview(unmemorizedOnly = unmemorizedOnly, sort = sort)
            },
            isReviewSavedFlow = entry.savedStateHandle.getStateFlow(VOCA_REVIEW_SAVED_KEY, false),
            onReviewSavedConsumed = { entry.savedStateHandle[VOCA_REVIEW_SAVED_KEY] = false },
        )
    }

    composable<VocaReview> {
        VocaReviewRoute(
            paddingValues = paddingValues,
            navigateUp = navController::navigateUp,
            navigateUpWithSaved = {
                navController.previousBackStackEntry?.savedStateHandle?.set(VOCA_REVIEW_SAVED_KEY, true)
                navController.navigateUp()
            },
        )
    }
}
