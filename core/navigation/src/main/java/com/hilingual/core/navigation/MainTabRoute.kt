package com.hilingual.core.navigation

import kotlinx.serialization.Serializable

sealed interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Voca: MainTabRoute

    @Serializable
    data object Community : MainTabRoute

    @Serializable
    data object My : MainTabRoute
}