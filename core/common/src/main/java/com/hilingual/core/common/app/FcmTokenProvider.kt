package com.hilingual.core.common.app

interface FcmTokenProvider {
    suspend fun getToken(): String
}
