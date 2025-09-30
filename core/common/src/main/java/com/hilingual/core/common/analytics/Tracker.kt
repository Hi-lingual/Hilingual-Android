package com.hilingual.core.common.analytics

interface Tracker {
    fun logEvent(name: String, properties: Map<String, Any>? = null)
}
