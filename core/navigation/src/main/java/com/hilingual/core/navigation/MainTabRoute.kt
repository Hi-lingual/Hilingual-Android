package com.hilingual.core.navigation

import kotlinx.serialization.Serializable

interface MainTabRoute : Route {

    @Serializable
    data object Community : MainTabRoute

    @Serializable
    data object My : MainTabRoute
}
