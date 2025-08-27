package com.hilingual.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.presentation.mypage.ProfileEditRoute
import kotlinx.serialization.Serializable

@Serializable
data object ProfileEdit

fun NavController.navigateToProfileEdit(
    navOptions: NavOptions? = null
) {
    navigate(
        route = ProfileEdit,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.profileEditNavGraph(
    paddingValues: PaddingValues,
    navigateToSplash: () -> Unit
) {
    composable<ProfileEdit> {
        ProfileEditRoute(
            paddingValues = paddingValues,
            navigateToSplash = navigateToSplash
        )
    }
}
