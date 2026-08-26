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
