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
package com.hilingual.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.auth.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
data object Auth : Route

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Auth,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.authNavGraph(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    navigateToOtp: () -> Unit
) {
    composable<Auth> {
        AuthRoute(
            paddingValues = paddingValues,
            navigateToHome = navigateToHome,
            navigateToOnboarding = navigateToOnboarding,
            navigateToOtp = navigateToOtp,
            animatedVisibilityScope = this
        )
    }
}
