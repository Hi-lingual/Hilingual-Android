/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.hilingual.BuildConfig
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.analytics.UserIdentityTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class AmplitudeTracker @Inject constructor(
    @ApplicationContext private val context: Context,
) : Tracker, UserIdentityTracker {

    private val amplitude: Amplitude? = run {
        if (BuildConfig.DEBUG) return@run null

        Amplitude(
            Configuration(
                apiKey = BuildConfig.AMPLITUDE_API_KEY,
                context = context,
                // 미설정 시 서버가 id 최소 길이 5자를 적용해, 자릿수가 짧은 userId를 가진
                // 이벤트가 400으로 폐기되고 익명 사용자만 남는다.
                minIdLength = 1,
            ),
        )
    }

    override fun setUserId(userId: Long) {
        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Set userId: $userId")
            return
        }

        amplitude?.setUserId(userId.toString())
    }

    override fun clearUserId() {
        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Clear userId")
            return
        }

        amplitude?.setUserId(null)
    }

    override fun logGlobalAction(
        trigger: TriggerType,
        action: String,
        properties: Map<String, Any>,
        currentPage: Page?,
    ) {
        val eventName = "${trigger.value}_$action"
        val allProperties = properties.toMutableMap()

        if (currentPage != null) {
            allProperties["page"] = currentPage.pageName
        }

        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Tracking global action: $eventName, properties: $allProperties")
            return
        }

        amplitude?.track(eventName, allProperties)
    }

    override fun logPageAction(
        trigger: TriggerType,
        page: Page,
        action: String,
        properties: Map<String, Any>,
    ) {
        val eventName = "${trigger.value}_${page.pageName}.$action"

        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Tracking page action: $eventName, properties: $properties")
            return
        }

        amplitude?.track(eventName, properties.toMutableMap())
    }
}
