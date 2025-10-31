package com.hilingual.core.common.analytics

class FakeTracker : Tracker {
    override fun logEvent(trigger: TriggerType, page: Page, event: String) {}
    override fun logEvent(trigger: TriggerType, page: Page, event: String, properties: Map<String, Any>) {}
}
