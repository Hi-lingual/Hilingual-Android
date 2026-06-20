package com.hilingual.core.common.analytics

interface UserIdentityTracker {
    fun setUserId(userId: Long)
    fun clearUserId()
}
