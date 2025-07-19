package com.hilingual.presentation.mypage

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object MyPage : MainTabRoute

fun NavController.navigateToMyPage(
    navOptions: NavOptions? = null
) {
    navigate(
        route = MyPage,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues
) {
    composable<MyPage> {
        MypageScreen(paddingValues = paddingValues)
    }
}
