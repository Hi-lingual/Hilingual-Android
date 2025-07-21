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
