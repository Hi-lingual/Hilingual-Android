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

interface Tracker {
    // 페이지 비종속 액션 (공통 컴포넌트)
    // 포맷: {트리거유형}_{이벤트명}, TriggerType.NONE이면 {이벤트명}
    // 예: click_dropdown, bookmark_action
    fun logGlobalAction(
        trigger: TriggerType,
        action: String,
        properties: Map<String, Any> = emptyMap(),
        currentPage: Page? = null,
    )

    // 페이지 종속 액션 (특정 화면 전용)
    // 포맷: {트리거유형}_{화면}.{이벤트명}
    // 예: click_feedback.bookmark_action
    fun logPageAction(
        trigger: TriggerType,
        page: Page,
        action: String,
        properties: Map<String, Any> = emptyMap(),
    )
}
