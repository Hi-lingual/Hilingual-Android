package com.hilingual.presentation.community

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Community : MainTabRoute

fun NavController.navigateToCommunity(
    navOptions: NavOptions? = null
) {
    navigate(
        route = Community,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.communityNavGraph(
    paddingValues: PaddingValues
) {
    composable<Community> {
        CommunityScreen(paddingValues = paddingValues)
    }
}
