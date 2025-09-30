package com.hilingual.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.hilingual.BuildConfig
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.Tracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class AmplitudeTracker @Inject constructor(
    @ApplicationContext private val context: Context
) : Tracker {

    private val amplitude = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_API_KEY,
            context = context
        )
    )

    override fun logEvent(page: Page, action: String, properties: Map<String, Any>?) {
        val eventName = "${page.pageName}.$action"
        Timber.d("Tracking event: $eventName, properties: ${properties ?: "None"}")

        if (properties == null) {
            amplitude.track(eventName)
        } else {
            amplitude.track(eventName, properties.toMutableMap())
        }
    }
}
