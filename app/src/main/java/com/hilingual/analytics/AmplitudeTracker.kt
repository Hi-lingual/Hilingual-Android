package com.hilingual.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.hilingual.BuildConfig
import com.hilingual.core.common.analytics.Tracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmplitudeTracker @Inject constructor(
    @ApplicationContext private val context: Context,
) : Tracker {

    private val amplitude = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_API_KEY,
            context = context
        )
    )

    override fun logEvent(name: String, properties: Map<String, Any>?) {
        if (properties == null) {
            amplitude.track(name)
        } else {
            amplitude.track(name, properties.toMutableMap())
        }
    }
}
