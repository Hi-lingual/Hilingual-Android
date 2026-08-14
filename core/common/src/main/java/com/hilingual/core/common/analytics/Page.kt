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
package com.hilingual.core.common.analytics

enum class Page(val pageName: String) {
    // 홈
    HOME("home"),

    // 일기 작성
    WRITE_DIARY("write_diary"),

    // 피드백
    FEEDBACK("feedback"),
    FEEDBACK_LOADING("feedback_loading"),

    // AI 피드백
    AI_FEEDBACK("ai_feedback"),

    // 단어장
    VOCABULARY("vocabulary"),

    // 피드
    FEED("feed"),
    POSTED_DIARY("posted_diary"),

    // 나의 피드 프로필 (홈 상단 / 피드에서 진입)
    MY_FEED("my_feed"),

    // 타인의 피드 프로필 (피드에서 진입)
    USER_PROFILE("user_profile"),

    // 마이페이지
    MYPAGE("mypage"),

    // 온보딩
    ONBOARDING("onboarding"),
}
