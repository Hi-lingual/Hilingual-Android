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
package com.hilingual.presentation.main

import androidx.annotation.DrawableRes
import com.hilingual.core.navigation.MainTabRoute
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.community.Community
import com.hilingual.presentation.home.navigation.Home
import com.hilingual.presentation.mypage.MyPage
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
    //TODO: 피드로 변경
    COMMUNITY(
        iconRes = R.drawable.ic_community_24,
        route = Community,
        label = "커뮤니티"
    ),
    MY(
        iconRes = R.drawable.ic_my_24,
        route = MyPage,
        label = "마이"
    );

    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}
