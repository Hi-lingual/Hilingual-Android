package com.hilingual.presentation.main

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.MainTabRoute.Community
import com.hilingual.core.navigation.MainTabRoute.My
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.home.navigation.Home
import com.hilingual.presentation.voca.navigation.Voca

internal enum class MainTab(
    @DrawableRes val iconRes: Int,
    val route: MainTabRoute,
    val label: String
) {
    HOME(
        iconRes = R.drawable.ic_home_24,
        route = Home,
        label = "홈"
    ),
    VOCA(
        iconRes = R.drawable.ic_book_24,
        route = Voca,
        label = "단어장"
    ),
    COMMUNITY(
        iconRes = R.drawable.ic_community_24,
        route = Community,
        label = "커뮤니티"
    ),
    MY(
        iconRes = R.drawable.ic_my_24,
        route = My,
        label = "마이"
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}