package com.hilingual.core.common.analytics

interface Tracker {
    fun logEvent(page: Page, event: String, properties: Map<String, Any>? = null)
}
