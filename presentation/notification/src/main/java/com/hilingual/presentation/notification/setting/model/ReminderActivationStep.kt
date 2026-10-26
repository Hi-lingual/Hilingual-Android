package com.hilingual.presentation.notification.setting.model

internal enum class ReminderActivationStep {
    REQUIRE_NOTIFICATION_PERMISSION,
    REQUIRE_EXACT_ALARM_PERMISSION,
    READY,
    ;

    companion object {
        fun resolve(
            isNotificationGranted: Boolean,
            isExactAlarmGranted: Boolean,
        ): ReminderActivationStep = when {
            !isNotificationGranted -> REQUIRE_NOTIFICATION_PERMISSION
            !isExactAlarmGranted -> REQUIRE_EXACT_ALARM_PERMISSION
            else -> READY
        }
    }
}
