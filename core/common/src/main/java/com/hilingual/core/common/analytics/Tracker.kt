package com.hilingual.core.common.analytics

interface Tracker {
    fun logEvent(trigger: TriggerType, page: Page, event: String)
    fun logEvent(trigger: TriggerType, page: Page, event: String, properties: Map<String, Any>)
}
