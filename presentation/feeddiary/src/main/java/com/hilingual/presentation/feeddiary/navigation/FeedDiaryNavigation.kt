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
package com.hilingual.presentation.feeddiary.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.feeddiary.FeedDiaryRoute
import kotlinx.serialization.Serializable

@Serializable
data class FeedDiary(
    val diaryId: Long
) : Route

fun NavController.navigateToFeedDiary(
    diaryId: Long,
    navOptions: NavOptions? = null
) {
    navigate(
        route = FeedDiary(diaryId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.feedDiaryNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: (showLikedDiaries: Boolean) -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit,
    navigateToVoca: () -> Unit
) {
    composable<FeedDiary> {
        FeedDiaryRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToMyFeedProfile = navigateToMyFeedProfile,
            navigateToFeedProfile = navigateToFeedProfile,
            navigateToVoca = navigateToVoca
        )
    }
}
