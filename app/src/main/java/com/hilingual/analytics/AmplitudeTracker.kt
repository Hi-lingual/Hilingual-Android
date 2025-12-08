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
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class AmplitudeTracker @Inject constructor(
    @ApplicationContext private val context: Context
) : Tracker {

    private val amplitude: Amplitude? = run {
        if (BuildConfig.DEBUG) return@run null

        Amplitude(
            Configuration(
                apiKey = BuildConfig.AMPLITUDE_API_KEY,
                context = context
            )
        )
    }

    override fun logEvent(trigger: TriggerType, page: Page, event: String) {
        val eventName = "${trigger.value}_${page.pageName}.$event"

        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Tracking event: $eventName, properties: None")
            return
        }

        amplitude?.track(eventName)
    }

    override fun logEvent(
        trigger: TriggerType,
        page: Page,
        event: String,
        properties: Map<String, Any>
    ) {
        val eventName = "${trigger.value}_${page.pageName}.$event"

        if (BuildConfig.DEBUG) {
            Timber.tag("AmplitudeTracker").d("Tracking event: $eventName, properties: $properties")
            return
        }

        amplitude?.track(eventName, properties.toMutableMap())
    }
}
