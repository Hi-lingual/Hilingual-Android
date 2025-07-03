package com.hilingual.presentation.voca.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute.Voca
import com.hilingual.presentation.voca.VocaRoute

fun NavController.navigateToVoca(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Voca,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.vocaNavGraph(
    paddingValues: PaddingValues
) {
    composable<Voca> {
        VocaRoute(
            paddingValues = paddingValues
        )
    }
}