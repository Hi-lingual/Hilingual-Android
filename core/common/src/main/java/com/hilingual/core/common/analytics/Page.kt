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

    // 단어장
    VOCABULARY("vocabulary"),

    // 피드
    FEED("feed"),
    POSTED_DIARY("posted_diary"),
    MY_FEED("my_feed")
}
