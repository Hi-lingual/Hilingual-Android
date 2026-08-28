/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.core.common.widget

const val EXTRA_WIDGET_TYPE = "widget_type"

enum class WidgetType(val value: String) {
    DIARY_TOPIC("diary_topic"),
    STREAK("streak"),
    ;

    companion object {
        fun from(value: String?): WidgetType? = entries.firstOrNull { it.value == value }
    }
}

data class InstalledWidgetCount(
    val diaryTopic: Int,
    val streak: Int,
) {
    val total: Int = diaryTopic + streak
}
