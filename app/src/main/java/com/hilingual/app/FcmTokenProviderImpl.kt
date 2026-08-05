package com.hilingual.app

import com.google.firebase.messaging.FirebaseMessaging
import com.hilingual.core.common.app.FcmTokenProvider
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

internal class FcmTokenProviderImpl @Inject constructor() : FcmTokenProvider {
    override suspend fun getToken(): String = FirebaseMessaging.getInstance().token.await()
}
