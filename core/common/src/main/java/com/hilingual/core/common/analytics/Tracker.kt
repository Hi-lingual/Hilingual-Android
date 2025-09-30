package com.hilingual.core.common.analytics

interface Tracker {
    fun logEvent(page: Page, action: String, properties: Map<String, Any>? = null)
}
