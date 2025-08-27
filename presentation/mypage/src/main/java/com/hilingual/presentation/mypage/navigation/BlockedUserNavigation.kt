package com.hilingual.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.presentation.mypage.BlockedUserRoute
import kotlinx.serialization.Serializable

@Serializable
data object BlockedUser

fun NavController.navigateToBlockedUser(
    navOptions: NavOptions? = null
) {
    navigate(
        route = BlockedUser,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.blockedUserNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit
) {
    composable<BlockedUser> {
        BlockedUserRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToProfile = navigateToProfile
        )
    }
}
